package com.example.desrsa_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class HelloController {

    @FXML
    private Button homeEncryptionBtn;
    @FXML
    private Button homeDecryptionBtn;

    @FXML
    private Label SelectedOpenFilelbl;

    @FXML
    private Button OpenFilebtn;

    @FXML
    private RadioButton DesRadio;

    @FXML
    private Button EncryptFilebtn;

    @FXML
    private Button OpenFilebtn2;

    @FXML
    private RadioButton RsaRadio;

    @FXML
    private ToggleGroup algorithm;
    @FXML
    private Label encInfo;

    public void HometoEncryption(ActionEvent e) throws IOException {
        homeEncryptionBtn.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("encryption.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage encStage = new Stage();
        encStage.setTitle("Encryption");
        encStage.setScene(scene);
        encStage.show();
    }

    public void HometoDecryption(ActionEvent e) throws IOException {
        homeDecryptionBtn.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("decryption.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage decStage = new Stage();
        decStage.setTitle("Decryption");
        decStage.setScene(scene);
        decStage.show();
    }

    public void SelectedFile() {
        FileChooser choosefile = new FileChooser();
        choosefile.setTitle("Open File");
        choosefile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = choosefile.showOpenDialog(null);
        if (selectedFile != null) {
            SelectedOpenFilelbl.setText(selectedFile.getAbsolutePath());
        } else {
            SelectedOpenFilelbl.setText("No file selected");
        }
    }

    public void PerformEncryption(ActionEvent e) throws NoSuchAlgorithmException, IOException {
        String use = "";
        String correctPath = correctFilePath(SelectedOpenFilelbl.getText());
        if (DesRadio.isSelected()) {
            use = "DES";
            DESEncrypt(correctPath);
        } else if (RsaRadio.isSelected()) {
            use = "RSA";
        }
        encInfo.setText("Encrypting: " + SelectedOpenFilelbl.getText() + "\nAlgorithm: " + use);
    }

    public static void DESEncrypt(String path) throws NoSuchAlgorithmException, IOException {
        KeyGenerator keygen = KeyGenerator.getInstance("DES");
        SecureRandom secureRandom = new SecureRandom();
        keygen.init(secureRandom);
        Key key = keygen.generateKey();

        File inputFile = new File(path);
        if (!inputFile.exists()) {
            System.out.println("File does not exist: " + path);
            return;
        }

        File outputFile = new File("C:\\des.close");
        try (BufferedReader data = new BufferedReader(new FileReader(inputFile));
             BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            String line;
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            writer.write(("Key : " + key).getBytes());

            while ((line = data.readLine()) != null) {
                byte[] inputBytes = line.getBytes(StandardCharsets.UTF_8);
                byte[] outputBytes = cipher.update(inputBytes);
                if (outputBytes != null) {
                    writer.write(outputBytes);
                }
            }
            byte[] finalBytes = cipher.doFinal();
            if (finalBytes != null) {
                writer.write(finalBytes);
            }
        } catch (IOException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte aByte : bytes) {
            hexString.append(Integer.toHexString(0xFF & aByte));
        }
        return hexString.toString();
    }

    private String correctFilePath(String path) {
        return path.replace("\\", "\\\\");
    }
}
