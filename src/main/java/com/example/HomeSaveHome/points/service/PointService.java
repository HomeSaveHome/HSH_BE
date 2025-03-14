package com.example.HomeSaveHome.points.service;

import com.example.HomeSaveHome.points.model.PointTransaction;
import com.example.HomeSaveHome.user.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {

    // 임시 저장소로 포인트 트랜잭션 내역을 관리 (실제 구현에서는 DB 연동)
    private List<PointTransaction> transactionHistory = new ArrayList<>();

    // 포인트 적립 처리
    public boolean addPoints(int amount) {
        // 현재 로그인된 사용자 정보는 실제로는 SecurityContext에서 가져와야 합니다.
        // 예시로, 임의 사용자에 포인트를 적립한다고 가정합니다.
        // User currentUser = userService.getCurrentUserByUsername();
        // currentUser.setPoint(currentUser.getPoint() + amount);

        // 포인트 적립 내역 추가 (여기서는 간단한 예시)
        PointTransaction pt = new PointTransaction();
        pt.setAmount(amount);
        pt.setDescription("포인트 적립");
        transactionHistory.add(pt);

        System.out.println("포인트 " + amount + "점 적립 처리");
        return true;
    }

    // 포인트 적립 내역 조회
    public List<PointTransaction> getPointHistory() {
        return transactionHistory;
    }
}
