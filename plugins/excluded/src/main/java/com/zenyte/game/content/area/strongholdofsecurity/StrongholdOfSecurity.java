package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.cores.ScheduledExternalizable;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.DefaultGson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;

/**
 * @author Kris | 4. sept 2018 : 00:50:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class StrongholdOfSecurity implements ScheduledExternalizable {

    private static final Logger log = LoggerFactory.getLogger(StrongholdOfSecurity.class);

    private static StrongholdOfSecurity.Question[] questions;

    public static Question getRandomQuestion() {
        if (questions == null) {
            throw new RuntimeException("SoS Questions haven't been initialized yet.");
        }
        return Utils.random(questions);
    }


    public static final class QuestionMessage {
        private final String[] strings;

        public QuestionMessage(final String... question) {
            strings = question;
        }

        public String[] getStrings() {
            return strings;
        }
    }


    public static final class AnswerMessage {
        private final String option;
        private final String[] message;

        public AnswerMessage(final String option, final String... message) {
            this.option = option;
            this.message = message;
        }

        public String getOption() {
            return option;
        }

        public String[] getMessage() {
            return message;
        }
    }


    public static class Question {
        private final QuestionMessage question;
        private final AnswerMessage[] answers;

        private Question(final QuestionMessage question, final AnswerMessage... answers) {
            this.question = question;
            this.answers = answers;
        }

        public QuestionMessage getQuestion() {
            return question;
        }

        public AnswerMessage[] getAnswers() {
            return answers;
        }
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public int writeInterval() {
        return 0;
    }

    @Override
    public void read(final BufferedReader reader) {
        questions = DefaultGson.fromGson(reader, Question[].class);
    }

    @Override
    public void write() {
    }

    @Override
    public String path() {
        return "data/stronghold of security questions.json";
    }

}
