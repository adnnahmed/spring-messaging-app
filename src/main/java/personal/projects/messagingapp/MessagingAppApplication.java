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
import personal.projects.messagingapp.message.MessageService;
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
    private final UnreadMessageStatsRepository unreadMessageStatsRepository;
	private final MessageService messageService;

	@Lazy
	public MessagingAppApplication(FolderRepository folderRepository, UnreadMessageStatsRepository unreadMessageStatsRepository,
								   MessageService messageService) {
		this.folderRepository = folderRepository;
        this.unreadMessageStatsRepository = unreadMessageStatsRepository;
		this.messageService = messageService;
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
		folderRepository.save(new Folder("adnnahmed", "Work", "blue"));
		folderRepository.save(new Folder("adnnahmed", "Personal", "green"));

		for (int i = 0; i < 5; i++) {
			messageService.sendMessage("adnnahmed", List.of("adnnahmed", "abc", "def"), "Subject " + i, "Body " + i);
		}
	}
}
