package personal.projects.messagingapp.controllers;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import personal.projects.messagingapp.folder.*;
import personal.projects.messagingapp.messagelist.MessageListItem;
import personal.projects.messagingapp.messagelist.MessageListItemRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class LandingPageController {

    private final FolderRepository folderRepository;
    private final FolderService folderService;
    private final MessageListItemRepository messageListItemRepository;

    @Lazy
    public LandingPageController(FolderRepository folderRepository, FolderService folderService, MessageListItemRepository messageListItemRepository) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.messageListItemRepository = messageListItemRepository;
    }

    @GetMapping("/")
    public String getLandingPage(@RequestParam(required = false) String folder, @AuthenticationPrincipal OAuth2User principal, Model model) {

        if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
            return "index";
        String userId = principal.getAttribute("login");

        //Fetch folders
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        model.addAttribute("unreadMessageStatsList", folderService.mapCountToLabels(userId));

        //Fetch messages
        if(!StringUtils.hasText(folder))
            folder = "Inbox";
        List<MessageListItem> messageList = messageListItemRepository.findAllByKey_IdAndKey_Label(userId, folder);
        PrettyTime prettyTime = new PrettyTime();
        messageList.forEach(messageListItem -> {
            UUID timeUUID = messageListItem.getKey().getTimeUUID();
            Date messageDateTime = new Date(Uuids.unixTimestamp(timeUUID));
            messageListItem.setAgoTimeString(prettyTime.format(messageDateTime));
        });
        model.addAttribute("messageList", messageList);
        model.addAttribute("folderName", folder);

        return "landing-page";
    }
}
