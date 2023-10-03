package com.tartu.sensorbot.activityHandlers;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tartu.sensorbot.R;
import com.tartu.sensorbot.bot.BotMessageTemplates;
import com.tartu.sensorbot.message.MessageAdapter;

public class ChatRecyclerViewHandler implements ActivityHandler {

  private final MessageAdapter messageAdapter;
  private final RecyclerView chatRecyclerView;
  private final Context context;

  public ChatRecyclerViewHandler(View rootView, Context context, String condition) {
    this.messageAdapter = new MessageAdapter(condition);
    this.context = context;
    this.chatRecyclerView = rootView.findViewById(R.id.chatRecyclerView);
  }

  @Override
  public void initialize() {
    messageAdapter.setScrollToBottomCallback(
        () -> chatRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1));
    chatRecyclerView.setAdapter(messageAdapter);
    chatRecyclerView.setLayoutManager(getLinearLayoutManager());

    // send initial bot message
    messageAdapter.addMessage(BotMessageTemplates.INITIAL_BOT_MESSAGE, true);
  }

  private LinearLayoutManager getLinearLayoutManager() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    layoutManager.setStackFromEnd(true);
    return layoutManager;
  }
}
