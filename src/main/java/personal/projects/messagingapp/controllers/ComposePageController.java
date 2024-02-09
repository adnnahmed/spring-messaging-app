package personal.projects.messagingapp.controllers;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import personal.projects.messagingapp.folder.Folder;
import personal.projects.messagingapp.folder.FolderRepository;
import personal.projects.messagingapp.folder.FolderService;
import personal.projects.messagingapp.message.MessageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ComposePageController {

    private final FolderRepository folderRepository;
    private final FolderService folderService;
    private final MessageService messageService;

    @Lazy
    public ComposePageController(FolderRepository folderRepository, FolderService folderService, MessageService messageService) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.messageService = messageService;
    }

    @GetMapping("/compose")
    public String getComposePage(@RequestParam(required = false) String to, @AuthenticationPrincipal OAuth2User principal, Model model) {

        if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
            return "index";
        String userId = principal.getAttribute("login");

        //Fetch folders
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<String> uniqueToIds = splitIds(to);
        model.addAttribute("toIds", String.join(", ", uniqueToIds));
        model.addAttribute("unreadMessageStatsList", folderService.mapCountToLabels(userId));

        return "compose-page";
    }

    private static List<String> splitIds(String to) {
        if (!StringUtils.hasText(to))
            return new ArrayList<>();
        String[] splitIds = to.split(",");
        return Arrays.stream(splitIds)
                .map(StringUtils::trimWhitespace)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @PostMapping("/sendEmail")
    public ModelAndView sendMessage(@RequestBody MultiValueMap<String, String> formData, @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
            return new ModelAndView("redirect:/");
        String userId = principal.getAttribute("login");

        String from = principal.getAttribute("login");
        List<String> toIds = splitIds(formData.getFirst("toIds"));
        String subject = formData.getFirst("subject");
        String body = formData.getFirst("body");

        messageService.sendMessage(from, toIds, subject, body);
        return new ModelAndView("redirect:/");
    }
}
