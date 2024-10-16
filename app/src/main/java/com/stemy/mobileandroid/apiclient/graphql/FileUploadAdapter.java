package com.stemy.mobileandroid.apiclient.graphql;
import androidx.annotation.NonNull;

import com.apollographql.apollo.api.Adapter;
import com.apollographql.apollo.api.CustomScalarAdapters;
import com.apollographql.apollo.api.DefaultUpload;
import com.apollographql.apollo.api.Upload;
import com.apollographql.apollo.api.json.JsonReader;
import com.apollographql.apollo.api.json.JsonWriter;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class FileUploadAdapter implements Adapter<Upload> {


    @Override
    public Upload fromJson(@NonNull JsonReader jsonReader, @NonNull CustomScalarAdapters customScalarAdapters) throws IOException {
        // Đọc dữ liệu từ JSON
        jsonReader.beginObject(); // Bắt đầu đọc đối tượng
        String fileName = null;
        long contentLength = 0;
        String contentType = null;

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "fileName":
                    fileName = jsonReader.nextString();
                    break;
                case "contentLength":
                    contentLength = jsonReader.nextLong();
                    break;
                case "contentType":
                    contentType = jsonReader.nextString();
                    break;
                default:
                    jsonReader.skipValue(); // Bỏ qua các giá trị không cần thiết
                    break;
            }
        }

        jsonReader.endObject(); // Kết thúc đọc đối tượng

        // Tạo đối tượng Upload với các thông tin đã đọc
        String finalContentType = contentType;
        long finalContentLength = contentLength;
        String finalFileName = fileName;
        return new Upload() {
            @Override
            public String getContentType() {
                return finalContentType;
            }

            @Override
            public long getContentLength() {
                return finalContentLength;
            }

            @Override
            public String getFileName() {
                return finalFileName;
            }

            @Override
            public void writeTo(@NonNull BufferedSink sink) {
                File file = new File(finalFileName);
                try (Source source = Okio.source(new FileInputStream(file))) {
                    sink.writeAll(source); // Ghi tất cả nội dung từ file vào sink
                }catch (IOException e){

                }
            }
        };
    }

    @Override
    public void toJson(@NonNull JsonWriter jsonWriter, @NonNull CustomScalarAdapters customScalarAdapters, Upload upload) throws IOException {
        // Ghi đối tượng Upload thành JSON
        jsonWriter.beginObject(); // Bắt đầu viết đối tượng

        jsonWriter.name("fileName").value(upload.getFileName());
        jsonWriter.name("contentLength").value(upload.getContentLength());
        jsonWriter.name("contentType").value(upload.getContentType());

        jsonWriter.endObject(); // Kết thúc viết đối tượng
    }
}
