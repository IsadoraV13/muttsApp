package com.muttsApp.repositories;

import com.muttsApp.POJOs.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    Chat findByChatId(int chatId);

    void deleteByChatId(int chatId);


}
