package nl.feedme.api.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableAlreadyOpenedException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableImageNotFoundException;
import nl.feedme.api.models.RestaurantTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

@Service
public class QRGeneratorService {

    @Value("${api-url}")
    private String apiUrl;

    private static final String LOGO = "feedmeSmall.png";

    public QRGeneratorService(){

    }

    private byte[] getQRCodeImage(String text, int width, int height, String imageName) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        HashMap<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION,  ErrorCorrectionLevel.H);

        // Generate QR
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        // Load QR image
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());

        ByteArrayOutputStream tempQR = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", tempQR);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", tempQR);
        System.out.println("Image (getQRCodeImage without overlay):");
        System.out.println(Base64.getEncoder().encodeToString(tempQR.toByteArray()));

        return tempQR.toByteArray();
        /*
        // Generate overly
        BufferedImage overly = getOverly(imageName);

        // Calculate the delta height and width between QR code and logo
        int deltaHeight = qrImage.getHeight() - overly.getHeight();
        int deltaWidth = qrImage.getWidth() - overly.getWidth();

        // Initialize combined image
        BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) combined.getGraphics();

        // Write QR code to new image at position 0/0
        g.drawImage(qrImage, 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Write logo into combine image at position (deltaWidth / 2) and
        // (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
        // the same space for the logo to be centered
        g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);



        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

        ImageIO.write(combined, "PNG", pngOutputStream);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
        */
    }

    public byte[] getPublicTableQR(long id) throws IOException, WriterException {
        String url = String.format("%s/api/tables/open/%d", apiUrl, id);
        byte[] image = getQRCodeImage(url, 500, 500, "feedMeMedium.png");
        System.out.println("Image (getPublicTableQR):");
        System.out.println(Base64.getEncoder().encodeToString(image));
        return image;

    }

    public byte[] getPrivateTableQr(String token) throws IOException, WriterException {
        String url = String.format("%s/api/tables/token/%s", apiUrl, token, "UTF-8");
        return getQRCodeImage(url, 300, 300, "feedmeSmall.png");
    }

    private BufferedImage getOverly(String LOGO) throws IOException {
        try {
            Resource resource = new ClassPathResource(LOGO);

            InputStream input = resource.getInputStream();

            File file = resource.getFile();
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new TableImageNotFoundException(e.getMessage());
        }
    }


    private MatrixToImageConfig getMatrixConfig() {
        // ARGB Colors
        // Check Colors ENUM
        return new MatrixToImageConfig(Colors.GRAY.getArgb(), Colors.WHITE.getArgb());
    }

    private String generateRandoTitle(Random random, int length) {
        return random.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public enum Colors {

        BLUE(0xFF40BAD0),
        RED(0xFFE91C43),
        PURPLE(0xFF8A4F9E),
        ORANGE(0xFFF4B13D),
        WHITE(0xFFFFFFFF),
        BLACK(0xFF000000),
        GRAY(0xFF424242);

        private final int argb;

        Colors(final int argb){
            this.argb = argb;
        }

        public int getArgb(){
            return argb;
        }
    }

}
