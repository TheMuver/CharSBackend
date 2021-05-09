package com.muver.chars.server.util;

import com.muver.chars.server.model.EncryptionPackage;
import com.muver.chars.server.util.EncodingType;
import com.muver.chars.server.util.DesEncoder;
import com.muver.chars.server.util.InsertEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class CharEncoder {

    public static EncryptionPackage execute(EncryptionPackage data) {
        try {
            switch (data.getOpType()) {
                case Insert:
                    data.setResult(encoding(data.getContainer(), data.getSecret(), data.getKey(), data.getEncType()));
                    break;
                case TakeOut:
                    data.setResult(decoding(data.getContainer(), data.getKey(), data.getEncType()));
                    break;
            }
            data.setState(OperationState.Ok);
        } catch (TooSmallContainerException e) {
            data.setState(OperationState.TooSmallContainer);
        } catch (InvalidChecksumException e) {
            data.setState(OperationState.InvalidCheckSum);
        } catch (Exception e) {
            data.setState(OperationState.EncryptionErr);
        }
        return data;
    }

    private static String encoding(String container, String message, String key, EncodingType type) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, TooSmallContainerException {
        String num = DesEncoder.encoding(message, key);
        switch (type) {
            case Standard:
                String length = DesEncoder.encoding(Integer.toString(message.length()), key);
                container = ReplaceEncoder.encoding(container, length);
                container = InsertEncoder.encoding(container, num, -1);
                break;
            case MaxCapacity:
                String l = ReplaceEncoder.maxCapacity(container);
                l = l.substring(0, l.length() > 10 ? l.length()-10 : l.length());
                int replaceLength = Math.min(l.length(), num.length()-1);
                container = ReplaceEncoder.encoding(container, num.substring(0, replaceLength));
                num = num.substring(replaceLength);
                if (!num.isEmpty())
                    container = InsertEncoder.encoding(container, num, -1);
                break;
            case OnlyInsert:
                container = InsertEncoder.encoding(container, num, -1);
                break;
            case OnlyReplace:
                container = ReplaceEncoder.encoding(container, num);
                break;
            default:
                break;
        }
        return container;
    }

    private static String decoding(String container, String key, EncodingType type) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidChecksumException {
        String message = "";
        switch (type){
            case Standard:
                String length = DesEncoder.decoding(ReplaceEncoder.decoding(container), key);
                message = InsertEncoder.decoding(container);
                message = DesEncoder.decoding(message, key);
                if (message.length() != Integer.parseInt(length))
                    throw new InvalidChecksumException();
                break;
            case MaxCapacity:
                message += ReplaceEncoder.decoding(container);
                message += InsertEncoder.decoding(container);
                message = DesEncoder.decoding(message, key);
                break;
            case OnlyInsert:
                message = InsertEncoder.decoding(container);
                message = DesEncoder.decoding(message, key);
                break;
            case OnlyReplace:
                message = ReplaceEncoder.decoding(container);
                message = DesEncoder.decoding(message, key);
                break;
            default:
                break;
        }
        return message;
    }
}
