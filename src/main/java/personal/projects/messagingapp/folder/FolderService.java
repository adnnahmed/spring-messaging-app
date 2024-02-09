package personal.projects.messagingapp.folder;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FolderService {

    private final UnreadMessageStatsRepository unreadMessageStatsRepository;

    @Lazy
    public FolderService(UnreadMessageStatsRepository unreadMessageStatsRepository) {
        this.unreadMessageStatsRepository = unreadMessageStatsRepository;
    }

    public List<Folder> fetchDefaultFolders(String userId) {
        return List.of(
                new Folder(userId, "Inbox", "blue"),
                new Folder(userId, "Sent", "green"),
                new Folder(userId, "Important", "green")
        );
    }

    public Map<String, Integer> mapCountToLabels(String userId) {
        List<UnreadMessageStats> unreadMessageStatsList = unreadMessageStatsRepository.findAllById(userId);
        return unreadMessageStatsList.stream().collect(Collectors.toMap(UnreadMessageStats::getLabel, UnreadMessageStats::getUnreadCount));
    }
}
