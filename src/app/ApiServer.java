package app;

import bst.BinarySearchTree;
import model.Student;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ApiServer {

    private static final BinarySearchTree bst = new BinarySearchTree();

    public static void main(String[] args) throws Exception {
        int port = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // List all OR Add new
        server.createContext("/students", ex -> {
            addCors(ex);

            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) { ok(ex, ""); return; }

            if ("GET".equalsIgnoreCase(ex.getRequestMethod())) {
                List<Student> list = bst.inorderList();
                ok(ex, toJsonArray(list));
                return;
            }

            if ("POST".equalsIgnoreCase(ex.getRequestMethod())) {
                String body = readBody(ex);
                Student s = parseStudentJson(body);
                if (s == null) { bad(ex, "Invalid JSON"); return; }

                if (s.getIndexNo() == null || s.getIndexNo().trim().isEmpty()) { bad(ex, "Index required"); return; }
                if (s.getName() == null || s.getName().trim().isEmpty()) { bad(ex, "Name required"); return; }
                if (!validMark(s.getPdsa()) || !validMark(s.getSe()) || !validMark(s.getDm2())) { bad(ex, "Marks must be 0-100"); return; }
                if (bst.search(s.getIndexNo()) != null) { bad(ex, "Student already exists"); return; }

                bst.insert(s);
                ok(ex, "{\"message\":\"added\"}");
                return;
            }

            methodNotAllowed(ex);
        });

        // Search / Update / Delete by id
        server.createContext("/student", ex -> {
            addCors(ex);

            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) { ok(ex, ""); return; }

            // /student?id=2025A001
            String id = queryParam(ex.getRequestURI().getQuery(), "id");
            if (id == null || id.trim().isEmpty()) { bad(ex, "id query param required"); return; }

            if ("GET".equalsIgnoreCase(ex.getRequestMethod())) {
                Student s = bst.search(id);
                if (s == null) { notFound(ex, "not found"); return; }
                ok(ex, toJson(s));
                return;
            }

            if ("DELETE".equalsIgnoreCase(ex.getRequestMethod())) {
                if (bst.search(id) == null) { notFound(ex, "not found"); return; }
                bst.delete(id);
                ok(ex, "{\"message\":\"deleted\"}");
                return;
            }

            if ("PUT".equalsIgnoreCase(ex.getRequestMethod())) {
                String body = readBody(ex);
                Student s = parseStudentJson(body);
                if (s == null) { bad(ex, "Invalid JSON"); return; }

                // Force key to be the id param (BST-safe)
                s = new Student(id, s.getName(), s.getPdsa(), s.getSe(), s.getDm2());

                if (bst.search(id) == null) { notFound(ex, "not found"); return; }
                if (s.getName() == null || s.getName().trim().isEmpty()) { bad(ex, "Name required"); return; }
                if (!validMark(s.getPdsa()) || !validMark(s.getSe()) || !validMark(s.getDm2())) { bad(ex, "Marks must be 0-100"); return; }

                bst.update(s);
                ok(ex, "{\"message\":\"updated\"}");
                return;
            }

            methodNotAllowed(ex);
        });

        server.start();
        System.out.println("API running at http://localhost:" + port);
        System.out.println("Frontend should call: http://localhost:" + port + "/students");
    }

    // ----------------- Helpers -----------------

    private static boolean validMark(double v) { return v >= 0 && v <= 100; }

    private static void addCors(HttpExchange ex) {
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    private static String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void ok(HttpExchange ex, String body) throws IOException {
        write(ex, 200, body);
    }

    private static void bad(HttpExchange ex, String msg) throws IOException {
        write(ex, 400, "{\"error\":\"" + escape(msg) + "\"}");
    }

    private static void notFound(HttpExchange ex, String msg) throws IOException {
        write(ex, 404, "{\"error\":\"" + escape(msg) + "\"}");
    }

    private static void methodNotAllowed(HttpExchange ex) throws IOException {
        write(ex, 405, "{\"error\":\"Method not allowed\"}");
    }

    private static void write(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Very small JSON parsing (expects: {"indexNo":"...","name":"...","pdsa":..,"se":..,"dm2":..})
    private static Student parseStudentJson(String json) {
        try {
            String indexNo = getJsonString(json, "indexNo");
            String name = getJsonString(json, "name");
            double pdsa = getJsonNumber(json, "pdsa");
            double se = getJsonNumber(json, "se");
            double dm2 = getJsonNumber(json, "dm2");
            return new Student(indexNo, name, pdsa, se, dm2);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getJsonString(String json, String key) {
        String k = "\"" + key + "\"";
        int i = json.indexOf(k);
        if (i < 0) return null;
        int c = json.indexOf(":", i);
        int q1 = json.indexOf("\"", c + 1);
        int q2 = json.indexOf("\"", q1 + 1);
        return json.substring(q1 + 1, q2);
    }

    private static double getJsonNumber(String json, String key) {
        String k = "\"" + key + "\"";
        int i = json.indexOf(k);
        if (i < 0) return 0;
        int c = json.indexOf(":", i);
        int end = json.indexOf(",", c + 1);
        if (end < 0) end = json.indexOf("}", c + 1);
        return Double.parseDouble(json.substring(c + 1, end).trim());
    }

    private static String toJson(Student s) {
        return "{"
                + "\"indexNo\":\"" + escape(s.getIndexNo()) + "\","
                + "\"name\":\"" + escape(s.getName()) + "\","
                + "\"pdsa\":" + s.getPdsa() + ","
                + "\"se\":" + s.getSe() + ","
                + "\"dm2\":" + s.getDm2() + ","
                + "\"average\":" + s.getAverage() + ","
                + "\"grade\":\"" + escape(s.getGrade()) + "\""
                + "}";
    }

    private static String toJsonArray(List<Student> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJson(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String queryParam(String query, String key) {
        if (query == null) return null;
        for (String part : query.split("&")) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) return kv[1];
        }
        return null;
    }
}
