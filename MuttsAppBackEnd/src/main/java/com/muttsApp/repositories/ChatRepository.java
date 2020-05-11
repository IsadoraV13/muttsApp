package com.muttsApp.repositories;

import com.muttsApp.POJOs.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    Chat findByChatId(int chatId);

    void deleteByChatId(int chatId);

    @Query(value = "select userId from userchat WHERE chatId = ?1", nativeQuery = true)
    List<Integer> findUserIdsinChat(int chatId);


}
