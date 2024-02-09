package personal.projects.messagingapp.controllers;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import personal.projects.messagingapp.folder.Folder;
import personal.projects.messagingapp.folder.FolderRepository;
import personal.projects.messagingapp.folder.FolderService;
import personal.projects.messagingapp.message.Message;
import personal.projects.messagingapp.message.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MessagePageController {

    private final FolderRepository folderRepository;
    private final FolderService folderService;
    private final MessageRepository messageRepository;

    @Lazy
    public MessagePageController(FolderRepository folderRepository, FolderService folderService, MessageRepository messageRepository) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/messages/{id}")
    public String getMessagePage(@PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {

        if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
            return "index";
        String userId = principal.getAttribute("login");

        //Fetch folders
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);

        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isEmpty())
            return "landing-page";
        Message message = messageOptional.get();
        String toIds = String.join(", ", message.getTo());
        model.addAttribute("message", message);
        model.addAttribute("toIds", toIds);

        return "message-page";
    }
}
