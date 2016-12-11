package com.example.cashie.cashie.Entity;

import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Alex on 12/11/2016.
 */

public class Profile {
    String uid;
    byte[] profilePhoto;
    String name;
    String email;
    boolean changed;

    public Profile() {
    }

    public Profile(InputStream profilePhoto, String name, String email, boolean changed) {
        try {
            if (profilePhoto != null)
                this.profilePhoto = readBytes(profilePhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.name = name;
        this.email = email;
        this.changed = changed;
    }

    public Profile(byte[] profilePhoto, String name, String email, boolean changed, boolean isByteArray) {
        if (isByteArray) {
            this.profilePhoto = profilePhoto;
        }
        this.name = name;
        this.email = email;
        this.changed = changed;
    }

    public String getUid() {
        return uid;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setProfilePhoto(InputStream profilePhoto) {
        try {
            if (profilePhoto != null)
                this.profilePhoto = readBytes(profilePhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setName(String name) {
        if (this.name != name) {
            this.name = name;
            this.setChanged(true);
        }
    }

    public void setEmail(String email) {
        if (this.email != email) {
            this.email = email;
            this.setChanged(true);
        }
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;

        while (inputStream != null && (len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
}