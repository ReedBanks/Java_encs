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

public class nHelloController {

    @FXML
    private Button homeEncryptionBtn;
    @FXML
    private Button homeDecryptionBtn;

    @FXML
    private Label SelectedOpenFilelbl;

    @FXML
    private Button OpenFilebtn;

    //encs
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
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("encryption.fxml"));
        Scene scene=new Scene(fxmlLoader.load());
        Stage encStage=new Stage();
        encStage.setTitle("Encryption");
        encStage.setScene(scene);
        encStage.show();

    }
    public void HometoDecryption(ActionEvent e) throws IOException {
        homeDecryptionBtn.getScene().getWindow().hide();
        FXMLLoader fxmlLoader=new FXMLLoader(HelloApplication.class.getResource("decryption.fxml"));
        Scene scene=new Scene(fxmlLoader.load());
        Stage decStage=new Stage();
        decStage.setTitle("Decryption");
        decStage.setScene(scene);
        decStage.show();
    }

    public void SelectedFile() throws NoSuchAlgorithmException {
        FileChooser choosefile=new FileChooser();
        choosefile.setTitle("Open .open file");
        //get file
        choosefile.getExtensionFilters().add(new FileChooser.ExtensionFilter(".open","*.open"));
//        String path=" "+choosefile.showOpenDialog(null).getAbsolutePath();
        File path=choosefile.showOpenDialog(new Stage());
        SelectedOpenFilelbl.setText(path.getAbsolutePath());

    }

    // encryot file and write to .close file
public  void PerformEncryption(ActionEvent e) throws NoSuchAlgorithmException, IOException {
    String use=" ";
    String CorrectPath=correctFilePath(SelectedOpenFilelbl.getText());
    if(DesRadio.isSelected()){
        use="DES";
        System.out.println(CorrectPath);
        DESEncrypt(CorrectPath);//correct filepath
    }
    else if(RsaRadio.isSelected()){
        use="RSA";
    }
   encInfo.setText("Encrypting : "+SelectedOpenFilelbl.getText()+"\nAlgorithm : "+use);

}


    public static void DESEncrypt(String path) throws NoSuchAlgorithmException, IOException {
        KeyGenerator keygen=KeyGenerator.getInstance("DES");
        SecureRandom secureRandom=new SecureRandom();
        keygen.init(secureRandom);
        Key key=keygen.generateKey();

        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File does not exist: " + path);
            return;
        }
        File outputFile=new File("C:\\Users\\Public\\Music\\des.close");

        try{
            BufferedReader data=new BufferedReader(new FileReader(file));
            String line;
            Cipher cipher= Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(cipher.ENCRYPT_MODE,key);//performing encryption with generated key
            BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(outputFile));

            byte[] msg=new byte[8192];
            int byteRead;

            System.out.println("key : "+key);
            //read data and encrypt piece by piece
            while ( (line=data.readLine()) != null) {
                System.out.println(line);
                byte[] inputBytes=line.getBytes(StandardCharsets.UTF_8);
                byte[] outputBytes=cipher.update(inputBytes);
                if(outputBytes != null){
                    writer.write(outputBytes);
                }
            }
            byte[] finalBytes=cipher.doFinal();

            //converting byte array to hex string
            String hash=bytesToHex(msg);
            System.out.println("Hash : "+hash);
            if (finalBytes != null) {
                writer.write(finalBytes);
            }
        }
        catch (IOException | NoSuchPaddingException | InvalidKeyException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    //converting byte arr to hex
    private static String bytesToHex(byte[] bytes){
        StringBuilder hexstring=new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            hexstring.append(Integer.toHexString(0xFF& bytes[i]));
        }
        return hexstring.toString();
    }
    private  String correctFilePath(String path) {
        return path.replace("\\", "\\\\");
    }


}