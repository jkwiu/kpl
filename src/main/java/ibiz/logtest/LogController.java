package ibiz.logtest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;
import com.amazonaws.services.kinesis.producer.UserRecordResult;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value="/logs")
public class LogController {

    final static Logger logger = LoggerFactory.getLogger(LogController.class);

    // kinesis variables
    @Value("${aws_kinesis_stream_name}")
    private String streamName;
    @Value("${aws_kinesis_partition_key}")
    private String partitionkey;

    @RequestMapping(value="/{type}/{num}", method=RequestMethod.GET)
    public void createLog(HttpServletResponse response, @PathVariable int num, @PathVariable String type) throws IOException, InterruptedException, ExecutionException {
        
        // For use STS
        // StsClient stsClient = StsClient.create();

        // AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
        //     .roleArn("arn:aws:iam::090902434044:role/kinesis-access-role")
        //     .roleSessionName("session-1")
        //     .build();

        // AssumeRoleResponse assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest);

        // Credentials sessionCredentials = assumeRoleResponse.credentials();

        // BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
        //     sessionCredentials.accessKeyId(), 
        //     sessionCredentials.secretAccessKey(), 
        //     sessionCredentials.sessionToken());

        //     KinesisProducerConfiguration config = new KinesisProducerConfiguration()
        //     .setRecordMaxBufferedTime(3000)
        //     .setMaxConnections(1)
        //     .setRequestTimeout(6000)
        //     .setRegion("ap-northeast-2")
        //     .setCredentialsProvider(new AWSStaticCredentialsProvider(basicSessionCredentials));

        // kinesis configuration
        KinesisProducerConfiguration config = new KinesisProducerConfiguration()
            .setRecordMaxBufferedTime(3000)
            .setMaxConnections(1)
            .setRequestTimeout(6000)
            .setRegion("ap-northeast-2");
        
        final KinesisProducer kinesis = new KinesisProducer(config);
        
        // result of log transfered
        FutureCallback<UserRecordResult> callBack = new FutureCallback<UserRecordResult>() {     
            @Override public void onFailure(Throwable t) {
                /* Analyze and respond to the failure  */ 
                System.out.println("----------------------------------- Fail -----------------------------------");
            };     
            @Override public void onSuccess(UserRecordResult result) { 
                /* Respond to the success */ 
                System.out.println("----------------------------------- Success -----------------------------------");
            };
        };

        String caseName = "";
        String sysUserName = System.getProperty("user.name");

        for (int i=1 ; i <= num ; i++)  {
            switch (type) {
                case "debug":
                    logger.debug("created debug log : " + i);
                    caseName = "debug";
                break;
                case "info": 
                    logger.info("created info log : " + i); 
                    caseName = "info";
                break;
                case "error": 
                    logger.error("created error log : " + i); 
                    caseName = "error";
                break;
                case "trace": 
                    logger.trace("created trace log : " + i); 
                    caseName = "trace";
                break;
                case "warn": 
                    logger.warn("created warn log : " + i); 
                    caseName = "warn";
                break;
            }            

            JSONObject jsonObject = new JSONObject("{\"logName\" : " + caseName + ", \"whoAmI\" : " + sysUserName + ", \"blank\" : \"test\"}");

            System.out.println("jsonObject: " + jsonObject);
            System.out.println("User's Name: " + sysUserName);
            String str = jsonObject.toString() + "\n";
            ByteBuffer data = ByteBuffer.wrap(str.getBytes("UTF-8"));
            ListenableFuture<UserRecordResult> f = kinesis.addUserRecord(streamName, partitionkey, data);

            Futures.addCallback(f, callBack);            
        }
        
        
        response.getWriter().println("log created..");
        response.getWriter().println("type : " + type);
        response.getWriter().println("amount : " + num);
    }

    public static void defaultLog(String type, int num) {
        for (int i=1 ; i <= num ; i++)  {
            switch (type) {
                case "debug": logger.debug("created debug log : " + i); break;
                case "info": logger.info("created info log : " + i); break;
                case "error": logger.error("created error log : " + i); break;
                case "trace": logger.trace("created trace log : " + i); break;
                case "warn": logger.warn("created warn log : " + i); break;
            }
        }
    }
}
