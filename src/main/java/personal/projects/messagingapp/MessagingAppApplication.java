package personal.projects.messagingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import personal.projects.messagingapp.configurations.DataStaxAstraProperties;
import personal.projects.messagingapp.folder.Folder;
import personal.projects.messagingapp.folder.FolderRepository;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@SpringBootApplication
public class MessagingAppApplication {

	private final FolderRepository folderRepository;

	@Lazy
	public MessagingAppApplication(FolderRepository folderRepository) {
		this.folderRepository = folderRepository;
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
	}
}
