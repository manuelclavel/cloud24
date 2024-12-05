/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author clavel
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;


@WebServlet(urlPatterns = { "/photos_list" })
public class ServletGetPhotosList extends HttpServlet {

	private static final long serialVersionUID = 1L;

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream(), "UTF-8"));
	
   	
	String bucketName = "eiu-sourcebucket-resized";
 
    
    AwsBasicCredentials awsBasicCredentials 
    = AwsBasicCredentials.create(
            "", 
            "");

    AwsCredentialsProvider awsCredentialsProvider;
    awsCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);

    S3Client s3Client = S3Client.builder()
  		  .credentialsProvider(awsCredentialsProvider)
  		  .region(Region.AP_SOUTHEAST_1)
  		  .build();
    
    ListObjectsRequest listObjects = ListObjectsRequest
            .builder()
            .bucket(bucketName)
            .build();

        ListObjectsResponse res = s3Client.listObjects(listObjects);
        List<S3Object> objects = res.contents();
        
        JSONArray objArray = new JSONArray();
        
        for (S3Object thumbnail : objects) {
        	JSONObject obj = new JSONObject();
        	obj.put("key", thumbnail.key());
        	obj.put("size", calKb(thumbnail.size()));
			objArray.put(obj);
        	
            }
        resp.setContentType("application/json");
        writer.write(objArray.toString());
    
}
//convert bytes to kbs.
private static long calKb(Long val) {
    return val/1024;
}
 
}

