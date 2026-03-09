package de.martin.jumpleaguegym.utils;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Base64Encoder {

    public static <T> String arrayToBase64(T[] items) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeInt(items.length);

        for (T item : items) {
            dataOutput.writeObject(item);
        }

        dataOutput.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public static <T> T[] arrayFromBase64(String data, Class<T> clazz) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(Base64.getDecoder().decode(data));

        BukkitObjectInputStream dataInput =
                new BukkitObjectInputStream(inputStream);

        int size = dataInput.readInt();
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) java.lang.reflect.Array.newInstance(clazz, size);

        for (int i = 0; i < size; i++) {
            arr[i] = (T) dataInput.readObject();
        }

        dataInput.close();
        return arr;
    }

    public static <T> String listToBase64(List<T> list) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeInt(list.size());

        for (T item : list) {
            dataOutput.writeObject(item);
        }

        dataOutput.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public static <T> List<T> listFromBase64(String data, Class<T> clazz) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(Base64.getDecoder().decode(data));

        BukkitObjectInputStream dataInput =
                new BukkitObjectInputStream(inputStream);

        int size = dataInput.readInt();
        @SuppressWarnings("unchecked")
        List<T> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add((T) dataInput.readObject());
        }

        dataInput.close();
        return list;
    }
}
