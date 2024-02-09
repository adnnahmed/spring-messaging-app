package personal.projects.messagingapp.message;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.projects.messagingapp.folder.UnreadMessageStatsRepository;
import personal.projects.messagingapp.messagelist.MessageListItem;
import personal.projects.messagingapp.messagelist.MessageListItemKey;
import personal.projects.messagingapp.messagelist.MessageListItemRepository;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageListItemRepository messageListItemRepository;
    private final UnreadMessageStatsRepository unreadMessageStatsRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          MessageListItemRepository messageListItemRepository, UnreadMessageStatsRepository unreadMessageStatsRepository) {
        this.messageRepository = messageRepository;
        this.messageListItemRepository = messageListItemRepository;
        this.unreadMessageStatsRepository = unreadMessageStatsRepository;
    }

    public void sendMessage(String from, List<String> to, String subject, String body) {

        Message message = new Message();
        message.setId(Uuids.timeBased());
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setBody(body);

        messageRepository.save(message);

        to.forEach(toId -> {
            MessageListItem messageListItem = createMessageListItem(to, from, subject, toId, message);
            messageListItemRepository.save(messageListItem);
            unreadMessageStatsRepository.incrementUnreadCount(toId, "Inbox");
        });

        MessageListItem sentMessageListItem = createMessageListItem(to, from, subject, from, message);
        sentMessageListItem.getKey().setLabel("Sent");
        sentMessageListItem.setUnread(false);
        messageListItemRepository.save(sentMessageListItem);
    }

    private static MessageListItem createMessageListItem(List<String> to, String from, String subject, String itemOwner, Message message) {
        MessageListItemKey key = new MessageListItemKey();
        key.setId(itemOwner);
        key.setLabel("Inbox");
        key.setTimeUUID(message.getId());

        MessageListItem messageListItem = new MessageListItem();
        messageListItem.setKey(key);
        messageListItem.setTo(to);
        messageListItem.setFrom(from);
        messageListItem.setSubject(subject);
        messageListItem.setUnread(true);

        return messageListItem;
    }
}
