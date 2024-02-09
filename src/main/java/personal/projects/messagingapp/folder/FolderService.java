package personal.projects.messagingapp.folder;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    public List<Folder> fetchDefaultFolders(String userId) {
        return List.of(
                new Folder(userId, "Inbox", "blue"),
                new Folder(userId, "Sent", "green"),
                new Folder(userId, "Important", "green")
        );
    }
}
