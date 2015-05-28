package fuzzycsv

import groovy.util.logging.Log4j
import secondstring.PhraseHelper

/**
 * Created by kay on 8/27/14.
 */
@Log4j
class Fuzzy {

    static int findBestPosition(def phrases, String header, float minScore) {
        phrases = phrases as List
        def csvColIdx = findPosition(phrases, header)
        if (csvColIdx == -1) {
            csvColIdx = findClosestPosition(phrases, header, minScore)
        }
        csvColIdx
    }

    static int findClosestPosition(def phrases, String phrase, float minScore) {
        phrases = phrases as List
        def ph = PhraseHelper.train(phrases)
        def newName = ph.bestInternalHit(phrase, minScore)

        if (newName == null) {
            if (log.isDebugEnabled())
                log.debug "getColumnPositionUsingHeuristic(): warning: no column match found:  [$phrase] = [$newName]"
            return -1
        }
        if (log.isDebugEnabled())
            log.debug "getColumnPositionUsingHeuristic(): ${ph.compare(newName, phrase)} heuristic: [$phrase] = [$newName]"
        return findPosition(phrases, newName)
    }

    static int findPosition(def phrases, String name) {
        phrases.findIndexOf { value -> value.toLowerCase().trim().equalsIgnoreCase(name.trim().toLowerCase()) }
    }
}
