# Sử dụng image Java chính thức
FROM openjdk:21-jdk-slim

# Tạo thư mục chứa ứng dụng trong container
WORKDIR /app

# Copy file .jar vào container (sửa tên file đúng với thực tế)
COPY target/SE2_Project-0.0.1-SNAPSHOT.jar app.jar

# Mở port 8080 nếu cần expose khi run standalone
EXPOSE 8080

# Lệnh để chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
