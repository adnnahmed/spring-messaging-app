package personal.projects.messagingapp;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import personal.projects.messagingapp.configurations.DataStaxAstraProperties;
import personal.projects.messagingapp.folder.Folder;
import personal.projects.messagingapp.folder.FolderRepository;
import personal.projects.messagingapp.folder.UnreadMessageStatsRepository;
import personal.projects.messagingapp.message.Message;
import personal.projects.messagingapp.message.MessageRepository;
import personal.projects.messagingapp.messagelist.MessageListItem;
import personal.projects.messagingapp.messagelist.MessageListItemKey;
import personal.projects.messagingapp.messagelist.MessageListItemRepository;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class MessagingAppApplication {

	private final FolderRepository folderRepository;
	private final MessageListItemRepository messageListItemRepository;
	private final MessageRepository messageRepository;
	private final UnreadMessageStatsRepository unreadMessageStatsRepository;

	@Lazy
	public MessagingAppApplication(FolderRepository folderRepository, MessageListItemRepository messageListItemRepository,
								   MessageRepository messageRepository, UnreadMessageStatsRepository unreadMessageStatsRepository) {
		this.folderRepository = folderRepository;
		this.messageListItemRepository = messageListItemRepository;
		this.messageRepository = messageRepository;
		this.unreadMessageStatsRepository = unreadMessageStatsRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(MessagingAppApplication.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init() {
		folderRepository.save(new Folder("adnnahmed", "Inbox", "blue"));
		folderRepository.save(new Folder("adnnahmed", "Sent", "green"));
		folderRepository.save(new Folder("adnnahmed", "Important", "red"));

		unreadMessageStatsRepository.incrementUnreadCount("adnnahmed", "Inbox");

		for (int i = 0; i < 5; i++) {
			MessageListItemKey key = new MessageListItemKey();
			key.setId("adnnahmed");
			key.setLabel("Inbox");
			key.setTimeUUID(Uuids.timeBased());

			MessageListItem messageListItem = new MessageListItem();
			messageListItem.setKey(key);
			messageListItem.setTo(List.of("adnnahmed-to", "abc", "def"));
			messageListItem.setFrom("adnnahmed-from");
			messageListItem.setSubject("Subject " + i);
			messageListItem.setUnread(true);

			messageListItemRepository.save(messageListItem);

			Message message = new Message();
			message.setId(key.getTimeUUID());
			message.setFrom(messageListItem.getFrom());
			message.setTo(messageListItem.getTo());
			message.setSubject(messageListItem.getSubject());
			message.setBody("Body " + i);
			messageRepository.save(message);
		}
	}
}
