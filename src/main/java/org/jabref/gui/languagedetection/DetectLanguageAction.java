package org.jabref.gui.languagedetection;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.jabref.gui.DialogService;
import org.jabref.gui.StateManager;
import org.jabref.gui.actions.ActionHelper;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.entry.BibEntry;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.LangDetectException;

public class DetectLanguageAction extends SimpleCommand {

    private final DialogService dialogService;
    private final StateManager stateManager;

    public DetectLanguageAction(DialogService dialogService, StateManager stateManager) {
        this.dialogService = dialogService;
        this.stateManager = stateManager;
        this.executable.bind(ActionHelper.needsEntriesSelected(1, stateManager));
    }

    @SuppressWarnings("checkstyle:WhitespaceAfter")
    @Override
    public void execute() {
        stateManager.getActiveDatabase().ifPresent(databaseContext -> {
            final List<BibEntry> entries = stateManager.getSelectedEntries();

            if (entries.size() != 1) {
                dialogService.notify(Localization.lang("This operation requires exactly one item to be selected."));
                return;
            }

            BibEntry entry = entries.get(0);

            HashMap<String, String> languageT = new HashMap<>();

            languageT.put("af", "Afrikaans");
            languageT.put("ar", "Arabic");
            languageT.put("bg", "Bulgarian");
            languageT.put("bn", "Bengali");
            languageT.put("cs", "Czech");
            languageT.put("da", "Danish");
            languageT.put("de", "German");
            languageT.put("el", "Greek");
            languageT.put("en", "English");
            languageT.put("es", "Spanish");
            languageT.put("et", "Estonian");
            languageT.put("fa", "Persian");
            languageT.put("fi", "Finnish");
            languageT.put("fr", "French");
            languageT.put("gu", "Gujarati");
            languageT.put("he", "Hebrew");
            languageT.put("hi", "Hindi");
            languageT.put("hr", "Croatian");
            languageT.put("hu", "Hungarian");
            languageT.put("id", "Indonesian");
            languageT.put("it", "Italian");
            languageT.put("ja", "Japanese");
            languageT.put("kn", "Kannada");
            languageT.put("ko", "Korean");
            languageT.put("lt", "Lithuanian");
            languageT.put("lv", "Latvian");
            languageT.put("mk", "Macedonian");
            languageT.put("ml", "Malayalam");
            languageT.put("mr", "Marathi");
            languageT.put("ne", "Nepali");
            languageT.put("nl", "Dutch");
            languageT.put("no", "Norwegian");
            languageT.put("pa", "Punjabi");
            languageT.put("pl", "Polish");
            languageT.put("pt", "Portuguese");
            languageT.put("ro", "Romanian");
            languageT.put("ru", "Russian");
            languageT.put("sk", "Slovak");
            languageT.put("sl", "Slovene");
            languageT.put("so", "Somali");
            languageT.put("sq", "Albanian");
            languageT.put("sv", "Swedish");
            languageT.put("sw", "Swahili");
            languageT.put("ta", "Tamil");
            languageT.put("te", "Telugu");
            languageT.put("th", "Thai");
            languageT.put("tl", "Tagalog");
            languageT.put("tr", "Turkish");
            languageT.put("uk", "Ukrainian");
            languageT.put("ur", "Urdu");
            languageT.put("vi", "Vietnamese");
            languageT.put("zh-c n", "Simplified Chinese");
            languageT.put("zh-tw", "Traditional Chinese");

            try {
                com.cybozu.labs.langdetect.DetectorFactory.loadProfile("/home/ivanandvenian/PORTUGAL/jabref/src/main/java/org/jabref/gui/languagedetection/profiles");
            } catch (LangDetectException e) {
                e.printStackTrace();
            }

            Optional<String> title = entry.getTitle();

            try {
                Detector detector = com.cybozu.labs.langdetect.DetectorFactory.create();
                detector.append(title.toString());
                String lang = detector.detect();
                dialogService.showInformationDialogAndWait("Detected Language", languageT.get(lang));
            } catch (com.cybozu.labs.langdetect.LangDetectException e) {
                e.printStackTrace();
                dialogService.showWarningDialogAndWait("Unable to read file", "ERROR: Languages File couldn't be read (com.cybozu.labs.langdetect.LangDetectException)");
            }
        });
    }
}
