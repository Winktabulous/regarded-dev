package com.zenyte.game.model.polls;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

/**
 * @author Kris | 26. march 2018 : 23:51.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AnsweredPoll {

	/**
     * The id of this poll.
     */
    @Expose
    private int pollId;

    /**
     * The title of the poll.
     */
    @Expose
    private String title;

    /**
     * The array of poll questions.
     */
    @Expose
    private AnsweredPollQuestion[] questions;

    /**
     * The date and time when the player submitted their answered to this poll.
     */
    @Expose
    private LocalDateTime submitDate;

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AnsweredPollQuestion[] getQuestions() {
        return questions;
    }

    public void setQuestions(AnsweredPollQuestion[] questions) {
        this.questions = questions;
    }

    public LocalDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(LocalDateTime submitDate) {
        this.submitDate = submitDate;
	}

    public static final class AnsweredPollQuestion {

        /**
         * The question in the poll.
         */
        @Expose
        private String question;

        /**
         * The answer the player chose.
         */
        @Expose
        private String answer;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }

}
