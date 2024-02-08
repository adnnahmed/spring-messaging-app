package personal.projects.messagingapp.controllers;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import personal.projects.messagingapp.folder.Folder;
import personal.projects.messagingapp.folder.FolderRepository;
import personal.projects.messagingapp.folder.FolderService;

import java.util.List;

@Controller
public class InboxPageController {

    private final FolderRepository folderRepository;
    private final FolderService folderService;

    @Lazy
    public InboxPageController(FolderRepository folderRepository, FolderService folderService) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
    }

    @GetMapping("/")
    public String getHomePage(@AuthenticationPrincipal OAuth2User principal, Model model) {

        if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
            return "index";
        String userId = principal.getAttribute("login");
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        return "landing-page";
    }
}
