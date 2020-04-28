package com.muttsApp.repositories;

import com.muttsApp.POJOs.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    Message findByMessageId(int messageId);

    List<Message> findByChatId(int chatId);

    Message findByOrderByMessageIdDesc(int chatId);

    @Query(value = "select * from message m WHERE m.chatId = ?1 order by m.messageId desc limit 1", nativeQuery = true)
    Message findByLastMsg(int chatId);


}
