package ro.ocr.webocr;


// Show uploaded file in this placeholder

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


// Implement both receiver that saves upload in a file and
// listener for successful upload
class ImageUploader implements Receiver, Upload.SucceededListener, Upload.Receiver {
    final Embedded image = new Embedded("Uploaded Image");


    public File file;

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
// Create upload stream
        FileOutputStream fos; // Stream to write to
        try {
// Open the file for writing.
            file = new File("/tmp/uploads/" + filename);
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
        return fos; // Return the output stream to write to
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
// Show the uploaded file in the image viewer
        image.setVisible(true);
        image.setSource(new FileResource(file));
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {

    }

    @Override
    public void close() {

    }

}
