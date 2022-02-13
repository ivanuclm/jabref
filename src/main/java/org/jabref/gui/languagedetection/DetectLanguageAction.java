package org.jabref.gui.languagedetection;

import java.util.List;
import java.util.Optional;

import javafx.beans.binding.BooleanExpression;

import org.jabref.gui.DialogService;
import org.jabref.gui.StateManager;
import org.jabref.gui.actions.ActionHelper;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.logic.l10n.Localization;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.preferences.PreferencesService;

import org.apache.tika.language.detect.LanguageDetector;

public class DetectLanguageAction extends SimpleCommand {

    private final DialogService dialogService;
    private final StateManager stateManager;
    private final PreferencesService preferences;

    public DetectLanguageAction(DialogService dialogService, StateManager stateManager, PreferencesService preferences) {
        this.dialogService = dialogService;
        this.stateManager = stateManager;
        this.preferences = preferences;

        BooleanExpression fieldIsSet = ActionHelper.isAnyFieldSetForSelectedEntry(
                List.of(StandardField.URL, StandardField.DOI, StandardField.URI, StandardField.EPRINT),
                stateManager);
        this.executable.bind(ActionHelper.needsEntriesSelected(1, stateManager).and(fieldIsSet));
    }

    @Override
    public void execute() {
        stateManager.getActiveDatabase().ifPresent(databaseContext -> {
            final List<BibEntry> entries = stateManager.getSelectedEntries();

            if (entries.size() != 1) {
                dialogService.notify(Localization.lang("This operation requires exactly one item to be selected."));
                return;
            }

            BibEntry entry = entries.get(0);

            // ToDo: Create dialog or menu to chose which one to open
            // URL - DOI - DOI - EPRINT
            /*
            Optional<String> link = entry.getField(StandardField.EPRINT);
            Field field = StandardField.EPRINT;
            if (entry.hasField(StandardField.URI)) {
                link = entry.getField(StandardField.URI);
                field = StandardField.URI;
            }
            if (entry.hasField(StandardField.DOI)) {
                link = entry.getField(StandardField.DOI);
                field = StandardField.DOI;
            }
            if (entry.hasField(StandardField.URL)) {
                link = entry.getField(StandardField.URL);
                field = StandardField.URL;
            }

            if (link.isPresent()) {
                try {
                    if (field.equals(StandardField.DOI) && preferences.getDOIPreferences().isUseCustom()) {
                        JabRefDesktop.openCustomDoi(link.get(), preferences, dialogService);
                    } else {
                        JabRefDesktop.openExternalViewer(databaseContext, preferences, link.get(), field);
                    }
                } catch (IOException e) {
                    dialogService.showErrorDialogAndWait(Localization.lang("Unable to open link."), e);
                }
            }
             */
            Optional<String> title = entry.getTitle();
            LanguageDetector detector = LanguageDetector.getDefaultLanguageDetector();
            String languageDetected = detector.detect(title.toString()).getLanguage();
            dialogService.notify(Localization.lang(languageDetected));
            /**
            try {
                /**
                Optional<String> title = entry.getTitle();
                LanguageDetector detector = LanguageDetectorBuilder.fromAllLanguages().build();
                Language detectedLanguage = detector.detectLanguageOf(title.toString());
                // Language detectedLanguage = detector.detectLanguageOf(String.valueOf(title));
                dialogService.notify(Localization.lang(detectedLanguage.toString()));


                Optional<String> title = entry.getTitle();
                // load all languages:
                List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

                // build language detector:
                LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard()).withProfiles(languageProfiles).build();

                //create a text object factory
                TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

                //query:
                TextObject textObject = textObjectFactory.forText("my text");
                Optional<LdLocale> lang = languageDetector.detect(textObject);
                 } catch (NoClassDefFoundError e) {
                dialogService.showErrorDialogAndWait(Localization.lang("Mierda"), e);
                } catch (IOException e2) {
                dialogService.showErrorDialogAndWait(Localization.lang("Peor aun"), e2);
                }
             */
        });
    }
}
