package com.zenyte.game.model.polls;

import com.google.gson.annotations.Expose;
import com.zenyte.game.model.polls.AnsweredPoll.AnsweredPollQuestion;

/**
 * @author Kris | 26. march 2018 : 3:34.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PollQuestion {

	/**
	 * The question in the poll.
	 */
	@Expose private String question;
	
	/**
	 * The hyperlink if applicable. The format should be as follows..
	 * The text user to overlay the link|https://zenyte.com/community/
	 */
	@Expose private String hyperlink;
	
	/**
	 * An array of available answers for this question.
	 */
	@Expose private PollAnswer[] answers;
	
	public String buildAnswers() {
		final StringBuilder answerBuilder = new StringBuilder();
		final int length = answers.length;
		for (int i = 0; i < 32; i++) {
			final PollAnswer answer = i >= length ? null : answers[i];
			if (answer == null)
				continue;
			answerBuilder.append(answer.getChoice()).append("|");
		}
		return answerBuilder.toString();
	}
	
	/**
	 * Gets the answers in the format used when opening the actual vote page.
	 * @return formatted poll answers.
	 */
	public String getFormattedPollAnswers() {
		final StringBuilder answerBuilder = new StringBuilder();
		final int length = answers.length;
		for (int i = 0; i < length; i++) {
			final PollAnswer answer = answers[i];
			if (answer == null)
				continue;
			answerBuilder.append(answer.getChoice()).append("|");
		}
		return answerBuilder.toString();
	}
	
	public int getAnswer(final AnsweredPoll poll, final int questionIndex) {
		if (poll == null)
			return 0;
		final AnsweredPollQuestion[] answers = poll.getQuestions();
		if (questionIndex >= answers.length)
			return 0;
		final String answer = answers[questionIndex].getAnswer();
		for (int i = 0; i < this.answers.length; i++) {
			if (this.answers[i].getChoice().equals(answer)) {
				return 1 << i;
			}
		}
		return 0;
	}
	
	public String getQuestion() {
	    return question;
	}
	
	public void setQuestion(String question) {
	    this.question = question;
	}
	
	public String getHyperlink() {
	    return hyperlink;
	}
	
	public void setHyperlink(String hyperlink) {
	    this.hyperlink = hyperlink;
	}
	
	public PollAnswer[] getAnswers() {
	    return answers;
	}
	
	public void setAnswers(PollAnswer[] answers) {
	    this.answers = answers;
	}

}
