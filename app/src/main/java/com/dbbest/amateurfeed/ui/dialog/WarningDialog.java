package com.dbbest.amateurfeed.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dbbest.amateurfeed.R;


public class WarningDialog extends BaseDialogFragment implements View.OnClickListener {

  private static final int NO_VALUE = -1;


  private static final String KEY_OK_TEXT = "OK_TEXT";
  private static final String KEY_CANCEL_TEXT = "CANCEL_TEXT";
  private static final String KEY_MESSAGE_TEXT = "MESSAGE_TEXT";
  private static final String KEY_MESSAGE_TEXT_STR = "MESSAGE_TEXT_STR";
  private static final String KEY_CANCELABLE = "CANCELABLE";
  private static final String KEY_OK_COLOR = "OK_COLOR";
  private static final String KEY_CANCEL_COLOR = "CANCEL_COLOR";
  private static final String KEY_MESSAGE_COLOR = "MESSAGE_COLOR";
  private static final String KEY_CODE = "CODE";

  private static final String KEY_LISTENER_ATTACHED = "LISTENER_ATTACHED";
  @StringRes
  private int okText;
  @StringRes
  private int cancelText;
  @StringRes
  private int messageText;
  private String messageTextStr;
  private boolean cancelable;
  @ColorRes
  private int okColor;
  @ColorRes
  private int cancelColor;
  @ColorRes
  private int messageColor;
  private int code;
  private OnWarningOkClickDialogListener okListener;
  private OnWarningDialogListener bothListener;

  private static WarningDialog instance(Builder builder) {
    Bundle args = new Bundle();
    args.putInt(KEY_OK_TEXT, builder.okText);
    args.putInt(KEY_CANCEL_TEXT, builder.cancelText);
    args.putInt(KEY_MESSAGE_TEXT, builder.messageText);
    args.putString(KEY_MESSAGE_TEXT_STR, builder.messageTextStr);
    args.putBoolean(KEY_CANCELABLE, builder.cancelable);
    args.putInt(KEY_OK_COLOR, builder.okColor);
    args.putInt(KEY_CANCEL_COLOR, builder.cancelColor);
    args.putInt(KEY_MESSAGE_COLOR, builder.messageColor);
    args.putInt(KEY_CODE, builder.code);
    args.putBoolean(KEY_LISTENER_ATTACHED, builder.listenerAttached);
    WarningDialog dialogFragment = new WarningDialog();
    dialogFragment.setArguments(args);
    return dialogFragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      okText = args.getInt(KEY_OK_TEXT);
      cancelText = args.getInt(KEY_CANCEL_TEXT);
      messageText = args.getInt(KEY_MESSAGE_TEXT);
      messageTextStr = args.getString(KEY_MESSAGE_TEXT_STR);

      cancelable = args.getBoolean(KEY_CANCELABLE);

      okColor = args.getInt(KEY_OK_COLOR);
      cancelColor = args.getInt(KEY_CANCEL_COLOR);
      messageColor = args.getInt(KEY_MESSAGE_COLOR);

      code = args.getInt(KEY_CODE);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    setCancelable(cancelable);

    View rootView = inflater.inflate(R.layout.warning_dialog, container, false);

    TextView okView = (TextView) rootView.findViewById(R.id.button_ok);
    TextView cancelView = (TextView) rootView.findViewById(R.id.button_cancel);
    TextView messageView = (TextView) rootView.findViewById(R.id.text_message);
    View dividerView = rootView.findViewById(R.id.view_divider);

    if (okText != NO_VALUE) {
      okView.setText(okText);
    }

    if (okColor != NO_VALUE) {
      okView.setTextColor(ContextCompat.getColor(getContext(), okColor));
    }

    if (cancelText != NO_VALUE) {
      cancelView.setText(cancelText);
    } else {
      cancelView.setVisibility(View.GONE);
      dividerView.setVisibility(View.GONE);
    }

    if (cancelColor != NO_VALUE) {
      cancelView.setTextColor(ContextCompat.getColor(getContext(), cancelColor));
    }

    if (messageText != NO_VALUE) {
      messageView.setText(messageText);
    } else if (messageTextStr != null) {
      messageView.setText(messageTextStr);
    }

    if (messageColor != NO_VALUE) {
      messageView.setTextColor(ContextCompat.getColor(getContext(), messageColor));
    }

    okView.setOnClickListener(this);
    cancelView.setOnClickListener(this);

    return rootView;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    boolean mListenerAttached =
        getArguments() != null && getArguments().getBoolean(KEY_LISTENER_ATTACHED);

    Fragment parentFragment = getParentFragment();
    if (mListenerAttached) {
      boolean attached = false;
      if (parentFragment != null) {
        if (parentFragment instanceof OnWarningDialogListener) {
          bothListener = (OnWarningDialogListener) parentFragment;
          attached = true;
        } else if (parentFragment instanceof OnWarningOkClickDialogListener) {
          okListener = (OnWarningOkClickDialogListener) parentFragment;
          attached = true;
        }
      }

      if (!attached) {
        if (getActivity() instanceof OnWarningDialogListener) {
          bothListener = (OnWarningDialogListener) getActivity();
          attached = true;
        } else if (getActivity() instanceof OnWarningOkClickDialogListener) {
          okListener = (OnWarningOkClickDialogListener) getActivity();
          attached = true;
        }
      }

      if (!attached) {
        throw new IllegalArgumentException(
            "Warning dialog OnWarningOkClickDialogListener was marked but not implemented!");
      }
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    okListener = null;
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.button_ok) {
      if (okListener != null) {
        okListener.onWarningDialogOkClicked(code);
      } else if (bothListener != null) {
        bothListener.onWarningDialogOkClicked(code);
      }
      dismissAllowingStateLoss();
    } else if (v.getId() == R.id.button_cancel) {
      if (bothListener != null) {
        bothListener.onWarningDialogCancelClicked(code);
      }
      dismissAllowingStateLoss();
    }
  }


  public final static class Builder {

    @StringRes
    private int okText = NO_VALUE;
    @StringRes
    private int cancelText = NO_VALUE;
    @StringRes
    private int messageText = NO_VALUE;
    private String messageTextStr;
    private boolean cancelable;
    @ColorRes
    private int okColor = NO_VALUE;
    @ColorRes
    private int cancelColor = NO_VALUE;
    @ColorRes
    private int messageColor = NO_VALUE;

    private int code = NO_VALUE;
    private boolean listenerAttached;

    public Builder setCode(int c) {
      this.code = c;
      return this;
    }

    public Builder setOkText(int okLabel) {
      this.okText = okLabel;
      return this;
    }

    public Builder setCancelText(int cancelLabel) {
      this.cancelText = cancelLabel;
      return this;
    }

    public Builder setMessageText(int mess
    ) {
      this.messageText = mess;
      return this;
    }

    public Builder setCancelable(boolean cancel) {
      this.cancelable = cancel;
      return this;
    }


    /**
     * listener will be attached on dialogFragment onAttach called
     * this method just specifies contract to callback interface
     *
     * @param listener on buttons click callback
     * @return current builder instance
     */
    public Builder setListener(OnWarningDialogListener listener) {
      listenerAttached = listener != null;
      return this;
    }

    /**
     * listener will be attached on dialogFragment onAttach called
     * this method just specifies contract to callback interface
     *
     * @param listener on buttons click callback
     * @return current builder instance
     */
    public Builder setListener(OnWarningOkClickDialogListener listener) {
      listenerAttached = listener != null;
      return this;
    }

    public WarningDialog build() {
      return WarningDialog.instance(this);
    }
  }

  interface OnWarningOkClickDialogListener {

    void onWarningDialogOkClicked(int dialogCode);
  }

  public interface OnWarningDialogListener extends OnWarningOkClickDialogListener {

    void onWarningDialogCancelClicked(int dialogCode);
  }

}
