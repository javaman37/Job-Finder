package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dao.VerificationTokenDAO;
import com.entity.VerificationToken;
import com.entity.User;
import java.util.Calendar;
import java.util.Date;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenDAO verificationTokenDAO;

    // Tìm kiếm token xác thực theo giá trị token
    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenDAO.findByToken(token);
    }

    // Lưu đối tượng token xác thực vào cơ sở dữ liệu
    @Override
    public void save(VerificationToken verificationToken) {
        verificationTokenDAO.save(verificationToken);
    }

    // Tạo token xác thực mới cho người dùng và xóa token cũ (nếu có)
    @Override
    public void createVerificationToken(User user, String token) {
        verificationTokenDAO.deleteByUser(user); // Xóa token cũ của người dùng nếu tồn tại
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(calculateExpiryDate(24 * 60)); // Đặt thời gian hết hạn là 24 giờ
        verificationTokenDAO.save(verificationToken);
    }

    // Tính toán ngày hết hạn cho token dựa trên số phút
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    // Xác thực token, kiểm tra xem token có hợp lệ hoặc đã hết hạn
    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenDAO.findByToken(token);
        if (verificationToken == null) {
            return "invalid"; // Token không tồn tại
        }

        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "expired"; // Token đã hết hạn
        }

        return "valid"; // Token hợp lệ
    }

    // Lấy thông tin người dùng từ token
    @Override
    public User getUserFromToken(String token) {
        VerificationToken verificationToken = verificationTokenDAO.findByToken(token);
        return verificationToken.getUser();
    }
}
