import com.google.gson.*;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.*;
import java.nio.file.Files;

public class HelloWorldServer {
    // TODO 1: Membuat baris kode untuk mengaktifkan sebuah peladen.
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HelloHandler());
        server.createContext("/fonts", new HelloHandler.FontHandler());
        server.start();
        System.out.println("Peladen berjalan pada Localhost port 8080");
    }

    // TODO 2: Menyusun main method untuk memproses penampilan data.
    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
        try {
            // TODO 3: Menyetel nama yang akan ditampilkan.
            String myName = "Arif Rahman Habibie";
            
            // TODO 4: Menampilkan waktu dan tanggal dengan format Bahasa Indonesia.
            Locale indonesian = Locale.of("id", "ID");
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", indonesian);
            String currentDate = dateFormat.format(new Date());

            // TODO 5: Menampilkan angka presentase waktu sejak 1 Januari hingga 31 Desember.
            LocalDate today = LocalDate.now();
            int dayOfYear = today.get(ChronoField.DAY_OF_YEAR);
            int totalDaysInYear = today.isLeapYear() ? 366 : 365;
            double percentagePassed = (dayOfYear / (double) totalDaysInYear) * 100;
            int roundedPercentage = (int) percentagePassed;
            
            // TODO 7: Menampilkan informasi suhu secara daring yang telah diambil dari internet.
            String city = "Medan";
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=3.5833&longitude=98.6667&current=temperature_2m";
            String jsonResponse = fetchDataFromApi(apiUrl);

            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonObject current = jsonObject.getAsJsonObject("current");
            JsonObject currentUnits = jsonObject.getAsJsonObject("current_units");

            double temperature = current.get("temperature_2m").getAsDouble();
            String temperatureUnit = currentUnits.get("temperature_2m").getAsString();
            String gmtTime = current.get("time").getAsString();

            // TODO 8: Menyetel format waktu WIB untuk menampilkan waktu pada informasi suhu udara.
            LocalDateTime gmtDateTime = LocalDateTime.parse(gmtTime);
            LocalDateTime wibDateTime = gmtDateTime.atZone(ZoneId.of("GMT"))
            .withZoneSameInstant(ZoneId.of("Asia/Jakarta"))
            .toLocalDateTime();
            Date wibDate = Date.from(wibDateTime.atZone(ZoneId.systemDefault()).toInstant());
            String formattedWIBTime = new SimpleDateFormat("HH:mm").format(wibDate) + " WIB";

            // TODO 11: Menampilkan semua informasi pada halaman index.html di folder resources.
            String htmlTemplate = loadHtmlTemplate("resources/index.html");
            String response = htmlTemplate
                .replace("{{name}}", myName)
                .replace("{{date}}", currentDate)
                .replace("{{percentage}}", roundedPercentage + "%")
                .replace("{{nextYear}}", String.valueOf(today.getYear() + 1))
                .replace("{{city}}", city)
                .replace("{{temperature}}", String.valueOf(temperature))
                .replace("{{temperatureUnit}}", temperatureUnit)
                .replace("{{formattedWIBTime}}", formattedWIBTime);

            byte[] responseBytes = response.getBytes("UTF-8");
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // TODO 6 : Menambahkan method untuk memproses logika menerima data dari internet.
    private String fetchDataFromApi(String apiUrl) throws IOException {
        StringBuilder response = new StringBuilder();
        URI uri = URI.create(apiUrl);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (Exception e) {
                System.err.println("Terjadi Kesalahan. Pesan Galat: " + e.getMessage());
                return "{\"current\":{\"temperature_2m\":0,\"time\":\"1970-01-01T00:00\"},\"current_units\":{\"temperature_2m\":\"°C\"}}";
            }
            return response.toString();
        }

    // TODO 10: Menambahkan method untuk memproses logika mengambil sumber daya berkas HTML pada proyek.
    private String loadHtmlTemplate(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder htmlContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            htmlContent.append(line);
        }
        reader.close();
        return htmlContent.toString();
    }

    // TODO 13: Menambahkan beberapa methods agar fon Calibri dapat ditampilkan pada perangkat lain.
    static class FontHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String fontFilePath = "resources" + exchange.getRequestURI().getPath();
            serveFile(exchange, fontFilePath);
        }
    }

    private static void serveFile(HttpExchange exchange, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        String mimeType = getMimeType(filePath);
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        exchange.getResponseHeaders().set("Content-Type", mimeType);
        exchange.sendResponseHeaders(200, fileBytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(fileBytes);
        os.close();
    }

    private static String getMimeType(String filePath) {
        if (filePath.endsWith(".html")) return "text/html; charset=UTF-8";
        if (filePath.endsWith(".woff2")) return "font/woff2";
        if (filePath.endsWith(".woff")) return "font/woff";
        if (filePath.endsWith(".ttf")) return "font/ttf";
        return "application/octet-stream";
        }}
}
