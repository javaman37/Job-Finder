//package com.test;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import com.config.JpaConfig;
//import com.dao.UserDAO;
//import com.entity.User;
//
//public class App {
//    public static void main(String[] args) {
//        // Tạo ApplicationContext từ JpaConfig
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);
//
//        // Lấy bean của UserDAO từ context
//        UserDAO UserDAO = context.getBean(UserDAO.class);
//
//        // Tạo một đối tượng User mới
//        User user = new User();
//        user.setAddress("528 HuynhTanPhat");
//        user.setDescription("Test User");
//        user.setEmail("test@example.com");
//        user.setFullName("Admin");
//        user.setPassword("password");
//        user.setPhoneNumber("123456789");
//        user.setStatus(1);
//
//        // Lưu user vào cơ sở dữ liệu
//        UserDAO.save(user);
//
//        // Lấy danh sách tất cả các user từ cơ sở dữ liệu và in ra
//        Iterable<User> users = UserDAO.findAll();
//        for (User u : users) {
//            System.out.println(u);
//        }
//
//        // Đóng context
//        context.close();
//    }
//}
