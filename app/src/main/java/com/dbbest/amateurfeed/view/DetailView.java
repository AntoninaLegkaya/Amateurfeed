package com.dbbest.amateurfeed.view;

import android.common.framework.IView;
import android.os.Bundle;


public interface DetailView extends IView {

   void addTagToItemDetail(Bundle data);

  void updateDetailsFields(Bundle data);

  void showSuccessEditNewsDialog();

  void showErrorEditNewsDialog();

  void showSuccessAddCommentDialog();

  void showErrorAddCommentDialog();

  void refreshFeedNews(Bundle data);

  void checkUpdateImage();

  void addCommentToBd(Bundle data);
}
