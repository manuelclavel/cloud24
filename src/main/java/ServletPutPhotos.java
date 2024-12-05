
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author clavel
 */
@WebServlet(urlPatterns = {"/photos"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 15 // 15 MB
// location            = "D:/Uploads"
)
public class ServletPutPhotos extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String JPG_TYPE = "jpg";
    private final String JPG_MIME = "image/jpeg";
    private final String PNG_TYPE = "png";
    private final String PNG_MIME = "image/png";

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String bucketName = "myfarm";
        String key = "vungtau0.jpg";

        //AwsBasicCredentials awsBasicCredentials 
        //= AwsBasicCredentials.create("AKIAT6ZS6LYZ6X6J34M3", "nynp4Xj33jfFxa8blYufNlsmNXK2m+0KUmC6W8p/");
        AwsBasicCredentials awsBasicCredentials
                = AwsBasicCredentials.create(
                        "AKIAXNOOUS7ISAN4XMIL",
                        "p8LXbBMfTmaZellyuORQ91Rk62nsjZEzKnoUsprv");
        
        AwsCredentialsProvider awsCredentialsProvider;
        awsCredentialsProvider
                = StaticCredentialsProvider.create(awsBasicCredentials);

        S3Client s3Client = S3Client.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.AP_SOUTHEAST_1)
                .build();

        //String fileName = null;
        //Get all the parts from request and write it to the file on server
        for (Part part : req.getParts()) {

            if (req.getPart("file") != null) {
                //part.write(uploadFilePath + File.separator + fileName);
                String objName = getFileName(part);
                InputStream inputStream = part.getInputStream();
                // From input to output
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buf = new byte[req.getContentLength()];
                int length;
                while ((length = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, length);
                }
                // Uploading
                putObject(s3Client, outputStream, bucketName, objName, "jpg");
                // get the URL
                S3Utilities utilities = s3Client.utilities();
                GetUrlRequest request = GetUrlRequest.builder().bucket(bucketName).key(objName).build();
                URL url = utilities.getUrl(request);
                // return the URL
                PrintWriter writer = resp.getWriter();
                writer.println(url.toString());

            }
        }

    }

    /**
     * Utility method to get file name from HTTP header content-disposition
     */
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");

        System.out.println("content-disposition header= " + contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    private void putObject(S3Client s3Client, ByteArrayOutputStream outputStream,
            String bucket, String key, String imageType) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Length", Integer.toString(outputStream.size()));
        if (JPG_TYPE.equals(imageType)) {
            metadata.put("Content-Type", JPG_MIME);
        } else if (PNG_TYPE.equals(imageType)) {
            metadata.put("Content-Type", PNG_MIME);
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .metadata(metadata)
                .build();

        // Uploading to S3 destination bucket
        try {
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(outputStream.toByteArray()));
        } catch (AwsServiceException e) {
            System.out.println(e);
        }
    }

}
