package com.sanrenxin.runxinnong.modules.wx.entity.message.customer;

public class TextMessage extends BaseMessage {

	private Text text;

	public TextMessage() {
	}

	public void setText(Text text) {
		this.text = text;
	}

	public Text getText() {
		return text;
	}

}
