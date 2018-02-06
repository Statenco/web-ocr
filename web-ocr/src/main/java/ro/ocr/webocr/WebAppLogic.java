package ro.ocr.webocr;

import com.sun.deploy.net.HttpResponse;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;


@SpringUI


public class WebAppLogic extends UI {


    //Strings to build URi for REST API calls.

    private static final String uriBasePreRegion = "https://";
    private static final String uriBasePostRegion = ".api.cognitive.microsoft.com/vision/";
    private static final String uriBaseOcr = "v1.0/ocr";
    OcrWebDesign ocrWebDesign = new OcrWebDesign();


    // <editor-fold defaultstate="collapsed" desc="Variables declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Statenco Dragos


    @Override
    protected void init(VaadinRequest vaadinRequest) {
//      OcrWebDesign ocrWebDesign = new OcrWebDesign();



      setContent(ocrWebDesign);

    }

    private void ocrImageButtonActionPerformed(Button evt) throws IOException {//GEN-FIRST:event_ocrImageButtonActionPerformed

        URL ocrImageUrl = new URL(ocrWebDesign.getTextFieldURL().toString());

        Resource res = new ThemeResource(ocrImageUrl.toString());
// Display the image without caption
        Image image = new Image(null, res);

        ocrWebDesign.getImage().setIcon((Resource) image);


        // Clear out the previous image, response, and caption, if any.
        ocrWebDesign.getImage().setIcon((Resource) new ImageIcon());
        ocrWebDesign.getResponseTextArea().setReadOnly(true);


        // Read the text in the image.
        JSONObject jsonObj = OcrImage(ocrImageUrl.toString());

        // A return of null indicates failure.
        if (jsonObj == null)

        {
            return;
        }
        System.out.println(jsonObj.toString(2));
        // Format and display the JSON response.
        ArrayList<String> lines = extractLines(jsonObj);


        ocrWebDesign.getResponseTextArea().setValue(String.join(" ", lines));
    }

    private JSONObject OcrImage(String imageUrl) {
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            // Create the URI to access the REST API call to read text in an image.
            String uriString = uriBasePreRegion + String.valueOf(ocrWebDesign.getSelectRegion().getSelectedItem()) + uriBasePostRegion + uriBaseOcr;
            URIBuilder uriBuilder = new URIBuilder(uriString);

            // Request parameters.
            uriBuilder.setParameter("language", String.valueOf(ocrWebDesign.getLanguageSelectorComboBox().getSelectedItem()));
            uriBuilder.setParameter("detectOrientation ", "true");

            // Prepare the URI for the REST API call.
            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.

            request.setHeader("Content-Type", String.valueOf(ocrWebDesign.getInputFileChooser().getSelectedItem()));
            request.setHeader("Ocp-Apim-Subscription-Key", ocrWebDesign.getSubscriptionPasswordField().getValue());

            // Request body.

            StringEntity reqEntity = new StringEntity("{\"url\":\"" + imageUrl + "\"}");
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = (HttpResponse) httpclient.execute(request);
            org.apache.http.HttpEntity entity = ((CloseableHttpResponse) response).getEntity();

            // If we got a response, parse it and display it.
            if (entity != null) {
                // Return the JSONObject.
                String jsonString = EntityUtils.toString(entity);


                return new JSONObject(jsonString);

            } else {
                // No response. Return null.
                return null;
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
            return null;
        }
    }


    private void ocrImageOpenButtonActionPerformed(Button etv) {//GEN-FIRST:event_ocrImageButtonActionPerformed

        ImageUploader receiver = new ImageUploader();
        // Create the upload with a caption and set receiver later
        Upload upload = new Upload("Upload Image Here", receiver);
        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);


        ocrWebDesign.getUploadImage().getReceiver();
        ocrWebDesign.getTextFieldURL().setValue(receiver.toString());


        // Read the text in the image.
        JSONObject jsonObj = OcrImage(String.valueOf(ocrWebDesign.getTextFieldURL()));

        // A return of null indicates failure.
        if (jsonObj == null)

        {
            return;
        }
        System.out.println(jsonObj.toString(2));
        // Format and display the JSON response.
        ArrayList<String> lines = extractLines(jsonObj);


        ocrWebDesign.getResponseTextArea().setValue(String.join(" ", lines));
    }//GEN-LAST:event_ocrImageButtonActionPerformed

    private JSONObject OcrImage(File imageUrl) {
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            // Create the URI to access the REST API call to read text in an image.
            String uriString = uriBasePreRegion + String.valueOf(ocrWebDesign.getSelectRegion().getSelectedItem()) + uriBasePostRegion + uriBaseOcr;
            URIBuilder uriBuilder = new URIBuilder(uriString);

            // Request parameters.
            uriBuilder.setParameter("language", String.valueOf(ocrWebDesign.getLanguageSelectorComboBox().getSelectedItem()));
            uriBuilder.setParameter("detectOrientation ", "true");

            // Prepare the URI for the REST API call.
            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.

            request.setHeader("Content-Type", String.valueOf(ocrWebDesign.getInputFileChooser().getSelectedItem()));
            request.setHeader("Ocp-Apim-Subscription-Key", ocrWebDesign.getSubscriptionPasswordField().getValue());

            // Request body.

            FileEntity reqEntity = new FileEntity(imageUrl, "application/octet-stream");
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = (HttpResponse) httpclient.execute(request);
            HttpEntity entity = (HttpEntity) ((CloseableHttpResponse) response).getEntity();

            // If we got a response, parse it and display it.
            if (entity != null) {
                // Return the JSONObject.
                String jsonString = EntityUtils.toString((org.apache.http.HttpEntity) entity);


                return new JSONObject(jsonString);

            } else {
                // No response. Return null.
                return null;
            }
        } catch (IOException | URISyntaxException e) {
            //Display error message.
            e.printStackTrace();
            return null;
        }
    }


    private ArrayList<String> extractLines(JSONObject object) {
        ArrayList<String> wordsList = new ArrayList<>();

        JSONObject regions = object.getJSONArray("regions").getJSONObject(0);

        JSONArray lines = regions.getJSONArray("lines");
        for (int i = 0; i < lines.length(); i++) {
            JSONObject line = lines.getJSONObject(i);

            JSONArray words = line.getJSONArray("words");
            for (int j = 0; j < words.length(); j++) {
                JSONObject word = words.getJSONObject(j);
                wordsList.add(word.getString("text"));
            }

        }


        return wordsList;
    }


}
