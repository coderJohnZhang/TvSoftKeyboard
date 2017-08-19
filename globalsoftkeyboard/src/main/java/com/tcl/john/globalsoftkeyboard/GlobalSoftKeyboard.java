package com.tcl.john.globalsoftkeyboard;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tcl.john.dialog.widget.ToastDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.tcl.john.globalsoftkeyboard.Constant.ARABIC_LANGUAGE;
import static com.tcl.john.globalsoftkeyboard.Constant.CHINESE_LANGUAGE;
import static com.tcl.john.globalsoftkeyboard.Constant.ENGLISH_LANGUAGE;
import static com.tcl.john.globalsoftkeyboard.Constant.FARSI_LANGUAGE;
import static com.tcl.john.globalsoftkeyboard.Constant.FRANCE_LANGUAGE;
import static com.tcl.john.globalsoftkeyboard.Constant.GERMAN_LANGUAGE;
import static com.tcl.john.globalsoftkeyboard.Constant.ITALY_LANGUAGE;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_0;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_1;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_2;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_3;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_4;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_5;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_6;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_7;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_8;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_9;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_BACK;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_BACKWARD;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_BLUE;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_CENTER;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_DOWN;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_F1;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_GREEN;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_GREEN_RECALL;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_MENU;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_MUTE;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_NEXT;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_OK;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_POWER;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_PREVIOUS;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_RED;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_UP;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_VOLDOWN;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_VOLUME_MUTE;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_VOLUP;
import static com.tcl.john.globalsoftkeyboard.Constant.KEY_YELLOW;
import static com.tcl.john.globalsoftkeyboard.Constant.RUSSIAN_LANGUAGE;
import static com.tcl.john.globalsoftkeyboard.Constant.SPANISH_LANGUAGE;
import static com.tcl.john.globalsoftkeyboard.Constant.TURKEY_LANGUAGE;

public class GlobalSoftKeyboard extends Dialog {

    private static final int RED_KEY_FOCUS = 0;
    private static final int BLUE_KEY_FOCUS = 1;
    private static final int GREEN_KEY_FOCUS = 2;

    private static final int ALPHABET_KEYBOARD = 0;
    private static final int DIGITAL_KEYBOARD = 1;
    private static final int SYMBOL_KEYBOARD = 2;

    private static final String CURSOR = "|";

    private static final int DEFAULT_HEIGHT = 350;

    private Context context;
    private KeyboardListener mKeyboardListener;
    private Integer iMaxLength;
    private TextWatcher textWatcher;
    private WindowManager.LayoutParams layoutParams;

    private EditText inputText;
    private Button blueKey;
    private PopupWindow popWindow;
    private ListView listView;

    private int layoutId;
    private int iCurTextCursor;
    private List<Button> btnList;
    private List<ImageButton> imageBtnList;
    private String[] keyLanguageList;
    private int languageIndex = 0;
    private Boolean bCapSwitch;
    private String text;
    private int keyboardType;
    private int vFocusView = 3;
    private boolean bUrl = false;

    public GlobalSoftKeyboard(Context context) {
        this(context, "", 1000, ALPHABET_KEYBOARD, null, false);
    }

    public GlobalSoftKeyboard(Context context, String text) {
        this(context, text, 1000, ALPHABET_KEYBOARD, null, false);
    }

    public GlobalSoftKeyboard(Context context, String text, boolean bUrl) {
        this(context, text, 1000, ALPHABET_KEYBOARD, null, bUrl);
    }

    public GlobalSoftKeyboard(Context context, String text, WindowManager.LayoutParams params) {
        this(context, text, 1000, ALPHABET_KEYBOARD, params, false);
    }

    public GlobalSoftKeyboard(Context context, String text, int MaxLen) {
        this(context, text, MaxLen, ALPHABET_KEYBOARD, null, false);
    }

    public GlobalSoftKeyboard(Context context, String text, int maxLen, int iType,
                              WindowManager.LayoutParams params, boolean bUrl) {
        super(context, R.style.ThemeKeyboard);
        this.context = context;
        if (text == null) {
            this.text = "";
        }
        this.text = text;
        this.iMaxLength = maxLen;
        if (params != null) {
            this.layoutParams = params;
        }

        assert text != null;
        iCurTextCursor = text.length();

        this.bUrl = bUrl;
        keyLanguageList = context.getResources().getStringArray(R.array.menu_language);

        btnList = new ArrayList<>();
        imageBtnList = new ArrayList<>();

        bCapSwitch = false;
        if (iType <= SYMBOL_KEYBOARD || iType >= ALPHABET_KEYBOARD) {
            keyboardType = iType;
        } else {
            keyboardType = ALPHABET_KEYBOARD;
        }

        initKeyboard();
        setKeyGroupListener();
    }

    public void focusPointButton() {
        Button imageButton = findViewById(R.id.pointButton);
        if (imageButton != null) {
            imageButton.requestFocus();
        }
    }

    public void showPasswordStyle() {
        if (inputText != null) {
            inputText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private float[] getDensity(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return new float[] {dm.widthPixels, dm.heightPixels, dm.density};
    }

    private void initLanPopwindow(String[] languageStr) {
        int[] location = new int[2];
        blueKey = findViewById(R.id.blueButton);
        blueKey.getLocationInWindow(location);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View mPopView = layoutInflater.inflate(R.layout.keyboard_lan_popupwindow_layout, null);
        popWindow = new PopupWindow(mPopView, blueKey.getWidth(), location[1] - 15);
        popWindow.setOutsideTouchable(true);
        popWindow.setFocusable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        listView = mPopView.findViewById(R.id.short_popwindow_lv);
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(context, R.layout.keyboard_textview, languageStr);
        listView.setAdapter(languageAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                languageIndex = position;
                text = inputText.getText().toString();
                vFocusView = BLUE_KEY_FOCUS;
                initKeyboard();
                popWindow.dismiss();
            }
        });

        listView.setOnKeyListener(new ListView.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int iSelect = listView.getSelectedItemPosition();
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KEY_UP:
                            iSelect--;
                            if (iSelect < 0) {
                                listView.setSelection(keyLanguageList.length - 1);
                            } else {
                                listView.onKeyDown(keyCode, event);
                            }
                            return true;
                        case KEY_DOWN:
                            iSelect++;
                            if (iSelect > keyLanguageList.length - 1) {
                                listView.setSelection(0);
                            } else {
                                listView.onKeyDown(keyCode, event);
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        popWindow.update();
        listView.setSelection(languageIndex);
        popWindow.showAtLocation(blueKey, Gravity.LEFT | Gravity.TOP, location[0], 15);
    }

    private void initKeyboard() {
        btnList.clear();
        imageBtnList.clear();
        String[] temStr = null;

        switch (languageIndex) {
            case CHINESE_LANGUAGE:
            case ENGLISH_LANGUAGE:
                switch (keyboardType) {
                    case ALPHABET_KEYBOARD:
                        if (bUrl) {
                            if (bCapSwitch) {
                                temStr = capsEngUrlCharBuffer;
                                temStr[temStr.length - 2] = keyLanguageList[ENGLISH_LANGUAGE];
                            } else {
                                temStr = EngUrlCharBuffer;
                                temStr[temStr.length - 2] = keyLanguageList[ENGLISH_LANGUAGE];
                            }
                            temStr[29] = "123";
                            layoutId = R.layout.keyboard_url;
                        } else {
                            if (bCapSwitch) {
                                temStr = capsEngCharBuffer;
                                temStr[temStr.length - 2] = keyLanguageList[ENGLISH_LANGUAGE];
                            } else {
                                temStr = EngCharBuffer;
                                temStr[temStr.length - 2] = keyLanguageList[ENGLISH_LANGUAGE];
                            }
                            temStr[26] = "123";
                            layoutId = R.layout.keyboard_english;
                        }
                        break;
                    case DIGITAL_KEYBOARD:
                        temStr = EngDigitCharBuffer;
                        temStr[temStr.length - 1] = keyLanguageList[ENGLISH_LANGUAGE];
                        layoutId = R.layout.keyboard_digital;
                        break;
                    case SYMBOL_KEYBOARD:
                        temStr = EngSymbolCharBuffer;
                        temStr[26] = "123";
                        temStr[27] = "abc";
                        temStr[30] = keyLanguageList[ENGLISH_LANGUAGE];
                        layoutId = R.layout.keyboard_digital;
                        break;
                }
                break;
            case ARABIC_LANGUAGE:
                switch (keyboardType) {
                    case ALPHABET_KEYBOARD:
                        if (bCapSwitch) {
                            temStr = ArabicSpecialBuffer;
                            temStr[temStr.length - 1] = keyLanguageList[ARABIC_LANGUAGE];
                            layoutId = R.layout.keyboard_digital;
                        } else {
                            temStr = ArabicCharBuffer;
                            layoutId = R.layout.keyboard_arabic_alphabet;
                        }

                        break;
                    case DIGITAL_KEYBOARD:
                        temStr = ArabicDigitCharBuf;
                        temStr[temStr.length - 1] = keyLanguageList[ARABIC_LANGUAGE];
                        layoutId = R.layout.keyboard_digital;
                        break;
                    case SYMBOL_KEYBOARD:
                        temStr = EngSymbolCharBuffer;
                        temStr[26] = "١ ٢ ٣";
                        temStr[27] = String.format(Locale.US, "%c%c%c%c%c", 0x62c, ' ', 0x628, ' ', 0x627);
                        temStr[30] = keyLanguageList[ARABIC_LANGUAGE];
                        layoutId = R.layout.keyboard_digital;
                        break;
                }
                break;
            case FARSI_LANGUAGE:
                switch (keyboardType) {
                    case ALPHABET_KEYBOARD:
                        if (bCapSwitch) {
                            temStr = ArabicSpecialBuffer;
                            temStr[temStr.length - 1] = keyLanguageList[FARSI_LANGUAGE];
                            layoutId = R.layout.keyboard_digital;
                        } else {
                            temStr = FarCharBuffer;
                            layoutId = R.layout.keyboard_arabic_alphabet;
                        }

                        break;
                    case DIGITAL_KEYBOARD:
                        temStr = ArabicDigitCharBuf;
                        temStr[temStr.length - 1] = keyLanguageList[FARSI_LANGUAGE];
                        layoutId = R.layout.keyboard_digital;
                        break;
                    case SYMBOL_KEYBOARD:
                        temStr = EngSymbolCharBuffer;
                        temStr[26] = "١ ٢ ٣";
                        temStr[27] = String.format(Locale.US, "%c%c%c%c%c", 0x62c, ' ', 0x628, ' ', 0x627);
                        temStr[30] = keyLanguageList[FARSI_LANGUAGE];
                        layoutId = R.layout.keyboard_digital;
                        break;
                }
                break;
            case TURKEY_LANGUAGE:
                switch (keyboardType) {
                    case ALPHABET_KEYBOARD:
                        if (bCapSwitch) {
                            temStr = capsEngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0x161, 0x11f, 0x10d);
                            temStr[temStr.length - 2] = keyLanguageList[TURKEY_LANGUAGE];
                        } else {
                            temStr = EngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0x161, 0x11f, 0x10d);
                            temStr[temStr.length - 2] = keyLanguageList[TURKEY_LANGUAGE];
                        }
                        layoutId = R.layout.keyboard_english;
                        break;
                    case DIGITAL_KEYBOARD:
                        temStr = EngDigitCharBuffer;
                        temStr[temStr.length - 1] = keyLanguageList[TURKEY_LANGUAGE];

                        layoutId = R.layout.keyboard_digital;
                        break;
                    case SYMBOL_KEYBOARD:
                        if (!bCapSwitch) {
                            temStr = TurSpecialBuffer;
                        } else {
                            temStr = capsTurSpecialBuffer;
                        }
                        layoutId = R.layout.keyboard_turkey;
                        break;
                }
                break;

            case GERMAN_LANGUAGE:
                switch (keyboardType) {
                    case ALPHABET_KEYBOARD:
                        if (bCapSwitch) {
                            temStr = capsEngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0xe4, 0xf4, 0xeb);
                            temStr[temStr.length - 2] = keyLanguageList[GERMAN_LANGUAGE];
                        } else {
                            temStr = EngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0xe4, 0xf4, 0xeb);
                            temStr[temStr.length - 2] = keyLanguageList[GERMAN_LANGUAGE];
                        }
                        layoutId = R.layout.keyboard_english;
                        break;
                    case DIGITAL_KEYBOARD:
                        temStr = EngDigitCharBuffer;
                        temStr[temStr.length - 1] = keyLanguageList[GERMAN_LANGUAGE];

                        layoutId = R.layout.keyboard_digital;
                        break;
                    case SYMBOL_KEYBOARD:
                        if (!bCapSwitch) {
                            temStr = GerSpecialBuffer;
                        } else {
                            temStr = capsGerSpecialBuffer;
                        }
                        layoutId = R.layout.keyboard_deutsch;
                        break;
                }
                break;
            case FRANCE_LANGUAGE:
                switch (keyboardType) {
                    case ALPHABET_KEYBOARD:
                        if (bCapSwitch) {
                            temStr = capsEngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0xe4, 0xf4, 0xeb);
                            temStr[temStr.length - 2] = keyLanguageList[FRANCE_LANGUAGE];
                        } else {
                            temStr = EngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0xe4, 0xf4, 0xeb);
                            temStr[temStr.length - 2] = keyLanguageList[FRANCE_LANGUAGE];
                        }
                        layoutId = R.layout.keyboard_english;
                        break;
                    case DIGITAL_KEYBOARD:
                        temStr = EngDigitCharBuffer;
                        temStr[temStr.length - 1] = keyLanguageList[FRANCE_LANGUAGE];

                        layoutId = R.layout.keyboard_digital;
                        break;
                    case SYMBOL_KEYBOARD:
                        if (!bCapSwitch) {
                            temStr = FraSpecialBuffer;
                        } else {
                            temStr = capsFraSpecialBuffer;
                        }
                        layoutId = R.layout.keyboard_france;
                        break;
                }
                break;
            case SPANISH_LANGUAGE:
                switch (keyboardType) {
                    case ALPHABET_KEYBOARD:
                        if (bCapSwitch) {
                            temStr = capsEngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0xe4, 0xf4, 0xeb);
                            temStr[temStr.length - 2] = keyLanguageList[SPANISH_LANGUAGE];
                        } else {
                            temStr = EngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0xe4, 0xf4, 0xeb);
                            temStr[temStr.length - 2] = keyLanguageList[SPANISH_LANGUAGE];
                        }
                        layoutId = R.layout.keyboard_english;
                        break;
                    case DIGITAL_KEYBOARD:
                        temStr = EngDigitCharBuffer;
                        temStr[temStr.length - 1] = keyLanguageList[SPANISH_LANGUAGE];

                        layoutId = R.layout.keyboard_digital;
                        break;
                    case SYMBOL_KEYBOARD:
                        if (!bCapSwitch) {
                            temStr = spaSpecialBuffer;
                        } else {
                            temStr = capsSpanSpecialBuffer;
                        }
                        layoutId = R.layout.keyboard_spanish;
                        break;
                }
                break;
            case ITALY_LANGUAGE:
                switch (keyboardType) {
                    case ALPHABET_KEYBOARD:
                        if (bCapSwitch) {
                            temStr = capsEngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0xe4, 0xf4, 0xeb);
                            temStr[temStr.length - 2] = keyLanguageList[ITALY_LANGUAGE];
                        } else {
                            temStr = EngCharBuffer;
                            temStr[26] = String.format(Locale.US, "%c%c%c", 0xe4, 0xf4, 0xeb);
                            temStr[temStr.length - 2] = keyLanguageList[ITALY_LANGUAGE];
                        }
                        layoutId = R.layout.keyboard_english;
                        break;
                    case DIGITAL_KEYBOARD:
                        temStr = EngDigitCharBuffer;
                        temStr[temStr.length - 1] = keyLanguageList[ITALY_LANGUAGE];

                        layoutId = R.layout.keyboard_digital;
                        break;
                    case SYMBOL_KEYBOARD:
                        if (!bCapSwitch) {
                            temStr = ItaSpecialBuffer;
                        } else {
                            temStr = capsItaSpecialBuffer;
                        }
                        layoutId = R.layout.keyboard_italy;
                        break;
                }
                break;
            case RUSSIAN_LANGUAGE:
                switch (keyboardType) {
                    case ALPHABET_KEYBOARD:
                        if (bCapSwitch) {
                            temStr = capsRussCharBuffer;
                            temStr[temStr.length - 2] = keyLanguageList[RUSSIAN_LANGUAGE];
                        } else {
                            temStr = RussCharBuffer;
                            temStr[temStr.length - 2] = keyLanguageList[RUSSIAN_LANGUAGE];
                        }
                        layoutId = R.layout.keyboard_russian;
                        break;
                    case DIGITAL_KEYBOARD:
                        temStr = EngDigitCharBuffer;
                        temStr[temStr.length - 1] = keyLanguageList[RUSSIAN_LANGUAGE];
                        layoutId = R.layout.keyboard_digital;
                        break;
                    case SYMBOL_KEYBOARD:
                        temStr = EngSymbolCharBuffer;
                        temStr[26] = "123";
                        temStr[27] = String.format(Locale.US, "%c%c%c", 0x430, 0x431, 0x432);
                        temStr[30] = keyLanguageList[RUSSIAN_LANGUAGE];
                        layoutId = R.layout.keyboard_digital;
                        break;
                }
                break;
        }

        Window window = getWindow();
        assert window != null;
        window.setContentView(layoutId);
        inputText = findViewById(R.id.inputText);
        inputText.setEnabled(false);
        inputText.setKeyListener(null);
        if (textWatcher != null) {
            inputText.addTextChangedListener(textWatcher);
        }
        String[] start = text.subSequence(0, iCurTextCursor).toString().split("\\|");
        String[] end = text.subSequence(iCurTextCursor, text.length()).toString().split("\\|");

        StringBuilder str = new StringBuilder();
        for (String aStart : start) {
            str.append(aStart);
        }
        for (String anEnd : end) {
            str.append(anEnd);
        }

        inputText.setText(str);
        if (iCurTextCursor > inputText.length()) {
            iCurTextCursor = inputText.length();
        }
        inputText.getText().insert(iCurTextCursor, Html.fromHtml("<font color=\"#B4B400\">" + CURSOR + "</font>"));

        if (iCurTextCursor < inputText.getText().length()) {
            inputText.setSelection(iCurTextCursor + 1);
        } else {
            inputText.setSelection(iCurTextCursor);
        }

        inputText.setSelected(true);
        ViewGroup keyboardView = findViewById(R.id.keyboardLayout);
        traversalViewGroup(keyboardView, btnList, imageBtnList);
        WindowManager.LayoutParams params = window.getAttributes();
        // set width by screen size and density
        float[] screen = getDensity(context);
        float width = screen[0];
        float density = screen[2];
        if (layoutParams != null) {
            params.width = (int) (layoutParams.width * density);
            params.height = (int) (layoutParams.height * density);
            params.gravity = layoutParams.gravity;
        } else {
            params.width = (int) width;
            params.height = (int) (DEFAULT_HEIGHT * density);
            params.gravity = Gravity.BOTTOM;
        }

        params.windowAnimations = R.style.AnimationsBottomInOut;

        window.setAttributes(params);

        View view;
        view = findViewById(R.id.redButton);
        if (view instanceof ImageButton) {
            ImageButton redKey = (ImageButton) view;
            if (bCapSwitch) {
                redKey.setImageResource(R.mipmap.capson);
            } else {
                redKey.setImageResource(R.mipmap.capsoff);
            }
        }

        assert temStr != null;
        for (int i = 0; i < temStr.length; i++) {
            btnList.get(i).setText(temStr[i]);
        }

        setKeyGroupListener();
        View focusView;
        switch (vFocusView) {
            case RED_KEY_FOCUS:
                focusView = findViewById(R.id.redButton);
                break;
            case BLUE_KEY_FOCUS:
                focusView = findViewById(R.id.blueButton);
                break;
            case GREEN_KEY_FOCUS:
                focusView = findViewById(R.id.greenButton);
                break;
            default:
                focusView = btnList.get(0);
        }
        focusView.requestFocus();

        // ARABIC & FARSI no upper/lower case.
        if ((languageIndex == ARABIC_LANGUAGE || languageIndex == FARSI_LANGUAGE) && keyboardType == ALPHABET_KEYBOARD) {
            findViewById(R.id.redButton).setVisibility(View.GONE);
        } else {
            findViewById(R.id.redButton).setVisibility(View.VISIBLE);
        }
    }

    private void setKeyGroupListener() {
        for (int i = 0; i < btnList.size(); i++) {
            btnList.get(i).setOnClickListener(clickListener);
        }

        for (int i = 0; i < imageBtnList.size(); i++) {
            imageBtnList.get(i).setOnClickListener(clickListener);
        }
    }

    private void traversalViewGroup(ViewGroup viewGroup, List<Button> btnList, List<ImageButton> imageBtnList) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View v = viewGroup.getChildAt(i);
            if (v instanceof Button) {
                Button btn = (Button) viewGroup.getChildAt(i);
                btn.setPadding(0, 0, 0, 0);
                btnList.add((Button) viewGroup.getChildAt(i));
            }
            if (v instanceof ImageButton) {
                imageBtnList.add((ImageButton) viewGroup.getChildAt(i));
            }
            if (v instanceof ViewGroup) {
                traversalViewGroup((ViewGroup) viewGroup.getChildAt(i), btnList, imageBtnList);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KEY_OK:
            case KEY_CENTER:
                respondEnterPress();
                break;
            case KEY_F1:
                F1keyDown();
                break;
            case KEY_RED:
                redKeyDown();
                break;
            case KEY_GREEN:
            case KEY_GREEN_RECALL:
                greenKeyDown();
                break;
            case KEY_YELLOW:
                yellowKeyDown();
                break;
            case KEY_BLUE:
                blueKeyDown();
                break;
            case KEY_PREVIOUS:
                previousKeyDown();
                break;
            case KEY_NEXT:
                nextKeyDown();
                break;
            case KEY_BACKWARD:
                backSpacePress();
                break;
            case KEY_0:
                setInsertCursor("0");
                break;
            case KEY_1:
                setInsertCursor("1");
                break;
            case KEY_2:
                setInsertCursor("2");
                break;
            case KEY_3:
                setInsertCursor("3");
                break;
            case KEY_4:
                setInsertCursor("4");
                break;
            case KEY_5:
                setInsertCursor("5");
                break;
            case KEY_6:
                setInsertCursor("6");
                break;
            case KEY_7:
                setInsertCursor("7");
                break;
            case KEY_8:
                setInsertCursor("8");
                break;
            case KEY_9:
                setInsertCursor("9");
                break;
            case KEY_BACK:
                backSpacePress();
                break;
            case KEY_POWER:
            case KEY_MENU:
                dismiss();
                break;
            case KEY_UP:
                return resetUpBottomFocus();
            case KEY_DOWN:
                return resetDownTopFocus();
            case KEY_VOLDOWN:
            case KEY_VOLUP:
            case KEY_MUTE:
            case KEY_VOLUME_MUTE:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean resetUpBottomFocus() {
        int iFocusIndex = -1;
        for (int i = 0; i < 10; i++) {
            if (btnList.get(i).hasFocus()) {
                iFocusIndex = i;
                break;
            }
        }

        if (iFocusIndex != -1) {
            switch (iFocusIndex) {
                case 0:
                    View view = findViewById(R.id.redButton);
                    if (view != null) {
                        if (view.getVisibility() != View.VISIBLE) {
                            findViewById(R.id.greenButton).requestFocus();
                        } else {
                            findViewById(R.id.redButton).requestFocus();
                        }
                    } else {
                        findViewById(R.id.greenButton).requestFocus();
                    }
                    return true;
                case 1:
                    findViewById(R.id.greenButton).requestFocus();
                    return true;
                case 2:
                    findViewById(R.id.previousButton).requestFocus();
                    return true;
                case 3:
                case 4:
                    findViewById(R.id.spaceButton).requestFocus();
                    return true;
                case 5:
                    findViewById(R.id.nextButton).requestFocus();
                    return true;
                case 6:
                case 7:
                    findViewById(R.id.yellowButton).requestFocus();
                    return true;
                case 8:
                    findViewById(R.id.blueButton).requestFocus();
                    return true;
                case 9:
                    findViewById(R.id.f1Button).requestFocus();
                    return true;
            }
        }
        return false;
    }

    private boolean resetDownTopFocus() {
        View view = null;
        for (int i = 0; i < btnList.size(); i++) {
            if (btnList.get(i).hasFocus()) {
                view = btnList.get(i);
                break;
            }
        }
        if (view == null) {
            for (int i = 0; i < imageBtnList.size(); i++) {
                if (imageBtnList.get(i).hasFocus()) {
                    view = imageBtnList.get(i);
                    break;
                }
            }
        }

        if (view != null) {
            if (view == findViewById(R.id.redButton)) {
                btnList.get(0).requestFocus();
            } else if (view == findViewById(R.id.greenButton)) {
                btnList.get(1).requestFocus();
            } else if (view == findViewById(R.id.previousButton)) {
                btnList.get(2).requestFocus();
            } else if (view == findViewById(R.id.spaceButton)) {
                btnList.get(4).requestFocus();
            } else if (view == findViewById(R.id.nextButton)) {
                btnList.get(5).requestFocus();
            } else if (view == findViewById(R.id.yellowButton)) {
                btnList.get(6).requestFocus();
            } else if (view == findViewById(R.id.blueButton)) {
                btnList.get(8).requestFocus();
            } else if (view == findViewById(R.id.f1Button)) {
                btnList.get(9).requestFocus();
            } else {
                return false;
            }
            return true;
        }

        return false;
    }

    /**
     * beyond max length tips
     */
    private void showMaxLengthMessage() {
        ToastDialog toast = new ToastDialog(context);
        toast.setMessage(R.string.STR_BEYOND_LENGTH + iMaxLength);
        toast.show(3000);
    }

    private void setInsertCursor(String str) {
        if (iMaxLength != null && (inputText.getText().length() - 1) >= iMaxLength) {
            showMaxLengthMessage();
        } else {
            String start = inputText.getText().subSequence(0, iCurTextCursor).toString();
            String end = inputText.getText().subSequence(iCurTextCursor, inputText.getText().length()).toString();

            String[] strTemp1 = start.split("\\|");
            StringBuilder temp1 = new StringBuilder();
            for (String aStrTemp1 : strTemp1) {
                temp1.append(aStrTemp1);
            }

            String[] strTemp2 = end.split("\\|");
            StringBuilder temp2 = new StringBuilder();
            for (String aStrTemp2 : strTemp2) {
                temp2.append(aStrTemp2);
            }

            inputText.setText(temp1 + str + temp2);
            if (iCurTextCursor < inputText.getText().length()) {
                inputText.getText().insert(iCurTextCursor + str.length(), Html.fromHtml("<font color=\"#B4B400\">" + CURSOR + "</font>"));
            } else {
                inputText.getText().insert(iCurTextCursor, Html.fromHtml("<font color=\"#B4B400\">" + CURSOR + "</font>"));
            }
            iCurTextCursor += str.length();
        }
    }

    private void respondEnterPress() {
        int iViewID;
        View currentView = getCurrentFocus();
        iViewID = currentView.getId();
        if (iViewID == R.id.inputText) {
        } else if (iViewID == R.id.backSpaceButton) {
            backSpacePress();
        } else if (iViewID == R.id.blueButton) {
            blueKeyDown();
        } else if (iViewID == R.id.redButton) {
            redKeyDown();
        } else if (iViewID == R.id.greenButton) {
            greenKeyDown();
        } else if (iViewID == R.id.yellowButton) {
            yellowKeyDown();
        } else if (iViewID == R.id.previousButton) {
            previousKeyDown();
        } else if (iViewID == R.id.nextButton) {
            nextKeyDown();
        } else if (iViewID == R.id.f1Button) {
            F1keyDown();
        } else if (iViewID == R.id.spaceButton) {
            spaceKeyDown();
        } else {
            getKeyChar(currentView);
        }
    }

    private void backSpacePress() {
        if (iCurTextCursor <= 0) {
            return;
        }
        if (iCurTextCursor <= 0 || iCurTextCursor >= inputText.getText().length()) {
            if (iCurTextCursor == inputText.getText().length()) {
                inputText.getText().delete(iCurTextCursor - 1, iCurTextCursor);
                iCurTextCursor--;
            }
        } else {
            inputText.getText().delete(iCurTextCursor - 1, iCurTextCursor);
            iCurTextCursor--;
        }
        backCursor(iCurTextCursor);
    }

    private void backCursor(int iCurTextCursor) {
        String[] strTemp1 = inputText.getText().subSequence(0, iCurTextCursor).toString().split("\\|");
        StringBuilder start = new StringBuilder();
        for (String aStrTemp1 : strTemp1) {
            start.append(aStrTemp1);
        }

        String[] strTemp2 = inputText.getText().subSequence(iCurTextCursor, inputText.getText().length()).toString().split("\\|");
        StringBuilder end = new StringBuilder();
        for (String aStrTemp2 : strTemp2) {
            end.append(aStrTemp2);
        }

        inputText.setText(start.toString() + end.toString());
        inputText.getText().insert(iCurTextCursor, Html.fromHtml("<font color=\"#B4B400\">" + CURSOR + "</font>"));
    }

    private void insertCursorLeft(int iSelect) {
        if (iSelect > 0) {
            String strStart = inputText.getText().subSequence(0, iSelect).toString();
            String strEnd = inputText.getText().subSequence(iSelect, inputText.getText().length()).toString();
            String[] text = strEnd.split("\\|");
            strEnd = "";
            for (String aText : text) {
                strEnd += aText;
            }
            inputText.setText(strStart + strEnd);
            inputText.getText().insert(iSelect + 1, Html.fromHtml("<font color=\"#B4B400\">" + CURSOR + "</font>"));
        } else {
            String[] input = inputText.getText().toString().split("\\|");
            String temp = null;
            for (String anInput : input) {
                temp += anInput;
            }

            if (temp != null) {
                iCurTextCursor = 0;
                inputText.setText(temp);
                inputText.getText().insert(0, Html.fromHtml("<font color=\"#B4B400\">" + CURSOR + "</font>"));
            }
        }
    }

    private void insertCursorRight(int iSelect) {
        if (iSelect > 0) {
            String strStart = inputText.getText().subSequence(0, iSelect).toString();
            String strEnd = inputText.getText().subSequence(iSelect, inputText.getText().length()).toString();
            String[] text = strStart.split("\\|");
            strStart = "";
            for (int i = 0; i < text.length; i++) {
                strStart += text[i];
            }
            String temp = strStart + strEnd;
            inputText.setText(temp);
            if (iSelect < inputText.getText().length()) {
                inputText.getText().insert(iSelect, Html.fromHtml("<font color=\"#B4B400\">" + CURSOR + "</font>"));
            } else {
                inputText.getText().insert(iSelect - 1, Html.fromHtml("<font color=\"#B4B400\">" + CURSOR + "</font>"));
            }
        }
    }

    private void getKeyChar(View currentView) {
        Button btn = (Button) currentView;

        setInsertCursor(btn.getText().toString());
    }

    private void spaceKeyDown() {

        setInsertCursor(" ");
    }

    private void yellowKeyDown() {

        String[] temp = inputText.getText().toString().split("\\|");
        String str = null;
        for (int i = 0; i < temp.length; i++) {
            str += temp[i];
        }
        if (mKeyboardListener != null) {
            mKeyboardListener.getText(str);
        }
        dismiss();
    }

    private void blueKeyDown() {
        initLanPopwindow(keyLanguageList);
    }

    private void previousKeyDown() {
        if (iCurTextCursor > 0) {
            iCurTextCursor--;
        }
        if (iCurTextCursor < inputText.getText().length()) {
            inputText.setSelection(iCurTextCursor + 1);
        } else {
            inputText.setSelection(iCurTextCursor);
        }
        insertCursorLeft(iCurTextCursor - 1);
    }

    private void greenKeyDown() {
        switch (languageIndex) {
            case ENGLISH_LANGUAGE:
            case ARABIC_LANGUAGE:
            case FARSI_LANGUAGE:
            case CHINESE_LANGUAGE:
            case RUSSIAN_LANGUAGE:
                if (keyboardType == ALPHABET_KEYBOARD) {
                    keyboardType = DIGITAL_KEYBOARD;
                } else if (keyboardType == DIGITAL_KEYBOARD) {
                    keyboardType = ALPHABET_KEYBOARD;
                } else if (keyboardType == SYMBOL_KEYBOARD) {
                    keyboardType = ALPHABET_KEYBOARD;
                    bCapSwitch = false;
                }
                break;
            case TURKEY_LANGUAGE:
            case GERMAN_LANGUAGE:
            case FRANCE_LANGUAGE:
            case SPANISH_LANGUAGE:
            case ITALY_LANGUAGE:
                if (keyboardType == ALPHABET_KEYBOARD) {
                    keyboardType = SYMBOL_KEYBOARD;
                } else if (keyboardType == DIGITAL_KEYBOARD) {
                    keyboardType = ALPHABET_KEYBOARD;
                } else if (keyboardType == SYMBOL_KEYBOARD) {
                    keyboardType = DIGITAL_KEYBOARD;
                    bCapSwitch = false;
                }
                break;
            default:
                return;
        }
        vFocusView = GREEN_KEY_FOCUS;
        text = inputText.getText().toString();
        initKeyboard();
    }

    private void nextKeyDown() {
        if (iCurTextCursor < inputText.getText().length()) {
            iCurTextCursor++;
        }
        if (iCurTextCursor < inputText.getText().length()) {
            inputText.setSelection(iCurTextCursor - 1);
        } else {
            inputText.setSelection(iCurTextCursor);
        }
        insertCursorRight(iCurTextCursor);
    }

    private void redKeyDown() {
        ImageButton redKey;
        switch (languageIndex) {
            case CHINESE_LANGUAGE:
            case ENGLISH_LANGUAGE:
            case RUSSIAN_LANGUAGE:
                if (keyboardType == ALPHABET_KEYBOARD) {
                    redKey = findViewById(R.id.redButton);
                    if (bCapSwitch) {

                        redKey.setImageResource(R.mipmap.capsoff);
                        bCapSwitch = !bCapSwitch;
                    } else {
                        redKey.setImageResource(R.mipmap.capson);
                        bCapSwitch = !bCapSwitch;
                    }
                } else if (keyboardType == DIGITAL_KEYBOARD) {
                    keyboardType = SYMBOL_KEYBOARD;
                } else if (keyboardType == SYMBOL_KEYBOARD) {
                    keyboardType = DIGITAL_KEYBOARD;
                }
                break;
            case ARABIC_LANGUAGE:
            case FARSI_LANGUAGE:
                if (keyboardType == ALPHABET_KEYBOARD) {
                    return;

                } else if (keyboardType == DIGITAL_KEYBOARD) {
                    keyboardType = SYMBOL_KEYBOARD;
                } else if (keyboardType == SYMBOL_KEYBOARD) {
                    keyboardType = DIGITAL_KEYBOARD;
                }

                break;
            case TURKEY_LANGUAGE:
            case GERMAN_LANGUAGE:
            case FRANCE_LANGUAGE:
            case SPANISH_LANGUAGE:
            case ITALY_LANGUAGE:
                if (keyboardType == ALPHABET_KEYBOARD) {
                    redKey = findViewById(R.id.redButton);
                    if (bCapSwitch) {

                        redKey.setImageResource(R.mipmap.capsoff);
                        bCapSwitch = !bCapSwitch;
                    } else {
                        redKey.setImageResource(R.mipmap.capson);
                        bCapSwitch = !bCapSwitch;
                    }
                } else if (keyboardType == DIGITAL_KEYBOARD) {
                    keyboardType = SYMBOL_KEYBOARD;
                } else {
                    if (keyboardType == SYMBOL_KEYBOARD) {
                        redKey = findViewById(R.id.redButton);
                        if (bCapSwitch) {

                            redKey.setImageResource(R.mipmap.capsoff);
                            bCapSwitch = !bCapSwitch;
                        } else {
                            redKey.setImageResource(R.mipmap.capson);
                            bCapSwitch = !bCapSwitch;
                        }
                    }
                }
                break;
            default:
                break;
        }
        vFocusView = RED_KEY_FOCUS;
        text = inputText.getText().toString();
        initKeyboard();
    }

    private void F1keyDown() {
        inputText.getText().delete(0, inputText.getText().length());
        inputText.setText(Html.fromHtml("<font color=\"#B4B400\">" + CURSOR + "</font>"));
        iCurTextCursor = 0;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.inputText) {
            } else if (id == R.id.backSpaceButton) {
                backSpacePress();
            } else if (id == R.id.blueButton) {
                blueKeyDown();
            } else if (id == R.id.redButton) {
                redKeyDown();
            } else if (id == R.id.greenButton) {
                greenKeyDown();
            } else if (id == R.id.yellowButton) {
                yellowKeyDown();
            } else if (id == R.id.previousButton) {
                previousKeyDown();
            } else if (id == R.id.nextButton) {
                nextKeyDown();
            } else if (id == R.id.f1Button) {
                F1keyDown();
            } else if (id == R.id.spaceButton) {
                spaceKeyDown();
            } else {
                getKeyChar(v);
            }
        }
    };

    public void setTextWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
        if (inputText != null) {
            inputText.addTextChangedListener(textWatcher);
        }
    }

    public void setUrlStyle(boolean bUrl) {
        this.bUrl = bUrl;
    }

    public void setDismissListener(OnDismissListener dismissListener) {
        this.setOnDismissListener(dismissListener);
    }

    public void setMaxLength(int iMaxLen) {
        this.iMaxLength = iMaxLen;
    }

    public void setKeyboardListenner(KeyboardListener listener) {
        this.mKeyboardListener = listener;
    }

    public interface KeyboardListener {
        void getText(String text);
    }

    private String EngCharBuffer[] = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m", "123", "Space", "OK",
            "English", "Clr(F1)"};
    private String capsEngCharBuffer[] = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M", "123", "Space", "OK",
            "English", "Clr(F1)"};

    private String EngUrlCharBuffer[] = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "www.", "z", "x", "c", "v", "b", "n", "m", ".com", "123",
            "Space", ".", "OK", "English", "Clr(F1)"};
    private String capsEngUrlCharBuffer[] = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "WWW.", "Z", "X", "C", "V", "B", "N", "M", ".COM", "123",
            "Space", ".", "OK", "English", "Clr(F1)"};

    private String EngDigitCharBuffer[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "/", ":", ";", "(", ")", "$", "&", "@", String.format(Locale.US, "%c", '\"'), "Clr(F1)", ".", ",",
            "?", "!", String.format(Locale.US, "%c", '\''), "#+=", "abc", "Space", "OK", "English"};
    private String EngSymbolCharBuffer[] = {"[", "]", "{", "}", "#", "%", "^", "*", "+", "=", "_", "\\", "|", "~", "<", ">", "€", "£", "¥", "•", "Clr(F1)", ".", ",", "?", "!",
            String.format(Locale.US, "%c", '\''), "123", "abc", "Space", "OK", "English"};

    private String ArabicCharBuffer[] = {"ض", "ص", "ث", "ق", "ف", "غ", "ع", "ه", "خ", "ح", "ج", "د", "ش", "س", "ي", "ب", "ل", "ا", "ت", "ن", "م", "ك", "ط",
            String.format(Locale.US, "%c%c%c%c%c", 0x652, ' ', 0x64B, ' ', 0x64D), "ئ", "ء", "ؤ", "ر", "ن", "ى", "ة", "و", "ز", "ظ", "١ ٢ ٣", "Space", "OK", "عربي", "Clr(F1)"};
    private String ArabicDigitCharBuf[] = {String.format(Locale.US, "%c", 0x661), String.format(Locale.US, "%c", 0x662), String.format(Locale.US, "%c", 0x663), String.format(Locale.US, "%c", 0x664),
            String.format(Locale.US, "%c", 0x665), String.format(Locale.US, "%c", 0x666), String.format(Locale.US, "%c", 0x667), String.format(Locale.US, "%c", 0x668),
            String.format(Locale.US, "%c", 0x669), String.format(Locale.US, "%c", 0x660), "-", "/", ":", ";", "(", ")", "$", "&", "@", String.format(Locale.US, "%c", '\"'), "Clr(F1)", ".",
            String.format(Locale.US, "%c", 0x60c), String.format(Locale.US, "%c", 0x61f), "!", String.format(Locale.US, "%c", '\''), "#+=",
            String.format(Locale.US, "%c%c%c%c%c", 0x62c, ' ', 0x628, ' ', 0x627), "Space", "OK", "عربي"};
    private String ArabicSpecialBuffer[] = {String.format(Locale.US, "%c", 0x653), String.format(Locale.US, "%c", 0x654), String.format(Locale.US, "%c", 0x655),
            String.format(Locale.US, "%c", 0x64f), String.format(Locale.US, "%c", 0x64e), String.format(Locale.US, "%c", 0x650), String.format(Locale.US, "%c", 0x640),
            String.format(Locale.US, "%c", 0x651), String.format(Locale.US, "%c", 0x64C), String.format(Locale.US, "%c", 0x64b), String.format(Locale.US, "%c", 0x64d),
            String.format(Locale.US, "%c", 0x652), ":", ";", "(", ")", "$", "&", "@", String.format(Locale.US, "%c", '\"'), "Clr(F1)", ".", String.format(Locale.US, "%c", 0x60c),
            String.format(Locale.US, "%c", 0x61f), "!", String.format(Locale.US, "%c", '\''), String.format(Locale.US, "%c%c%c%c%c", 0x62c, ' ', 0x628, ' ', 0x627), "١ ٢ ٣", "Space", "OK", "عربي"};

    private String FarCharBuffer[] = {"ض", "ص", "ث", "ق", "ف", "غ", "ع", "ه", "خ", "ح", "ج", "چ", "ش", "س", "ي", "ب", "ل", "ا", "ت", "ن", "م", "ک", "گ",
            String.format(Locale.US, "%c%c%c%c%c", 0x652, ' ', 0x64B, ' ', 0x64D), "ط", "ظ", "ز", "ر", "›", "ڈ", "ئ", "و", "پ", "د", "١ ٢ ٣", "Space", "OK", "فارسي", "Clr(F1)"};

    private String TurSpecialBuffer[] = {String.format(Locale.US, "%c", 0xE2), String.format(Locale.US, "%c", 0x12b), String.format(Locale.US, "%c", 0xec), String.format(Locale.US, "%c", 0xed),
            String.format(Locale.US, "%c", 0xee), String.format(Locale.US, "%c", 0xef), String.format(Locale.US, "%c", 0x12f), String.format(Locale.US, "%c", 0x131),
            String.format(Locale.US, "%c", 0xf8), String.format(Locale.US, "%c", 0xf2), String.format(Locale.US, "%c", 0xf3), String.format(Locale.US, "%c", 0xf4),
            String.format(Locale.US, "%c", 0xf5), String.format(Locale.US, "%c", 0xf6), String.format(Locale.US, "%c", 0x14d), String.format(Locale.US, "%c", 0x153),
            String.format(Locale.US, "%c", 0xf9), String.format(Locale.US, "%c", 0xfa), String.format(Locale.US, "%c", 0xfb), String.format(Locale.US, "%c", 0xfc),
            String.format(Locale.US, "%c", 0x16b), String.format(Locale.US, "%c", 0x15b), String.format(Locale.US, "%c", 0x15f), String.format(Locale.US, "%c", 0x161),
            String.format(Locale.US, "%c", 0xdf), String.format(Locale.US, "%c", 0x11f), String.format(Locale.US, "%c", 0x107), String.format(Locale.US, "%c", 0x10d),
            String.format(Locale.US, "%c", 0xe7), "Clr(F1)", "123", "Space", "OK", "Turkish"};
    private String capsTurSpecialBuffer[] = {String.format(Locale.US, "%c", 0xC2), String.format(Locale.US, "%c", 0x12A), String.format(Locale.US, "%c", 0xcc), String.format(Locale.US, "%c", 0xcd),
            String.format(Locale.US, "%c", 0xce), String.format(Locale.US, "%c", 0xcf), String.format(Locale.US, "%c", 0x12e), String.format(Locale.US, "%c", 0x130),
            String.format(Locale.US, "%c", 0xd8), String.format(Locale.US, "%c", 0xd2), String.format(Locale.US, "%c", 0xd3), String.format(Locale.US, "%c", 0xd4),
            String.format(Locale.US, "%c", 0xd5), String.format(Locale.US, "%c", 0xd6), String.format(Locale.US, "%c", 0x14C), String.format(Locale.US, "%c", 0x152),
            String.format(Locale.US, "%c", 0xd9), String.format(Locale.US, "%c", 0xda), String.format(Locale.US, "%c", 0xdb), String.format(Locale.US, "%c", 0xdc),
            String.format(Locale.US, "%c", 0x16a), String.format(Locale.US, "%c", 0x15a), String.format(Locale.US, "%c", 0x15e), String.format(Locale.US, "%c", 0x160),
            String.format(Locale.US, "%c", 0xdf), String.format(Locale.US, "%c", 0x11e), String.format(Locale.US, "%c", 0x106), String.format(Locale.US, "%c", 0x10c),
            String.format(Locale.US, "%c", 0xc7), "Clr(F1)", "123", "Space", "OK", "Turkish"};

    private String GerSpecialBuffer[] = {String.format(Locale.US, "%c", 0xe0), String.format(Locale.US, "%c", 0xe1), String.format(Locale.US, "%c", 0xe2), String.format(Locale.US, "%c", 0xe3),
            String.format(Locale.US, "%c", 0xe4), String.format(Locale.US, "%c", 0xe5), String.format(Locale.US, "%c", 0xe6), String.format(Locale.US, "%c", 0x101),
            String.format(Locale.US, "%c", 0x117), String.format(Locale.US, "%c", 0x144), String.format(Locale.US, "%c", 0xf1), String.format(Locale.US, "%c", 0xf8),
            String.format(Locale.US, "%c", 0xf2), String.format(Locale.US, "%c", 0xf3), String.format(Locale.US, "%c", 0xf4), String.format(Locale.US, "%c", 0xf5),
            String.format(Locale.US, "%c", 0xf6), String.format(Locale.US, "%c", 0x14d), String.format(Locale.US, "%c", 0x153), String.format(Locale.US, "%c", 0x15b),
            String.format(Locale.US, "%c", 0x161), String.format(Locale.US, "%c", 0xdf), String.format(Locale.US, "%c", 0x16b), String.format(Locale.US, "%c", 0xf9),
            String.format(Locale.US, "%c", 0xfa), String.format(Locale.US, "%c", 0xfb), String.format(Locale.US, "%c", 0xfc), "Clr(F1)", "123", "Space", "OK", "Deutsch"};

    private String capsGerSpecialBuffer[] = {String.format(Locale.US, "%c", 0xc0), String.format(Locale.US, "%c", 0xc1), String.format(Locale.US, "%c", 0xc2), String.format(Locale.US, "%c", 0xc3),
            String.format(Locale.US, "%c", 0xc4), String.format(Locale.US, "%c", 0xc5), String.format(Locale.US, "%c", 0xc6), String.format(Locale.US, "%c", 0x100),
            String.format(Locale.US, "%c", 0x116), String.format(Locale.US, "%c", 0x143), String.format(Locale.US, "%c", 0xd1), String.format(Locale.US, "%c", 0xd8),
            String.format(Locale.US, "%c", 0xd2), String.format(Locale.US, "%c", 0xd3), String.format(Locale.US, "%c", 0xd4), String.format(Locale.US, "%c", 0xd5),
            String.format(Locale.US, "%c", 0xd6), String.format(Locale.US, "%c", 0x14c), String.format(Locale.US, "%c", 0x152), String.format(Locale.US, "%c", 0x15a),
            String.format(Locale.US, "%c", 0x160), String.format(Locale.US, "%c", 0xdf), String.format(Locale.US, "%c", 0x16a), String.format(Locale.US, "%c", 0xd9),
            String.format(Locale.US, "%c", 0xda), String.format(Locale.US, "%c", 0xdb), String.format(Locale.US, "%c", 0xdc), "Clr(F1)", "123", "Space", "OK", "Deutsch"};

    private String FraSpecialBuffer[] = {String.format(Locale.US, "%c", 0xe0), String.format(Locale.US, "%c", 0xe1), String.format(Locale.US, "%c", 0xe2), String.format(Locale.US, "%c", 0xe3),
            String.format(Locale.US, "%c", 0xe4), String.format(Locale.US, "%c", 0xe5), String.format(Locale.US, "%c", 0xe6), String.format(Locale.US, "%c", 0x101),
            String.format(Locale.US, "%c", 0xaa), String.format(Locale.US, "%c", 0x107), String.format(Locale.US, "%c", 0x10d), String.format(Locale.US, "%c", 0xe7),
            String.format(Locale.US, "%c", 0xe8), String.format(Locale.US, "%c", 0xe9), String.format(Locale.US, "%c", 0xea), String.format(Locale.US, "%c", 0xeb),
            String.format(Locale.US, "%c", 0x113), String.format(Locale.US, "%c", 0x117), String.format(Locale.US, "%c", 0x119), String.format(Locale.US, "%c", 0x12b),
            String.format(Locale.US, "%c", 0xec), String.format(Locale.US, "%c", 0xed), String.format(Locale.US, "%c", 0xee), String.format(Locale.US, "%c", 0xef),
            String.format(Locale.US, "%c", 0x12f), String.format(Locale.US, "%c", 0xf8), String.format(Locale.US, "%c", 0xf2), String.format(Locale.US, "%c", 0xf3),
            String.format(Locale.US, "%c", 0xf4), String.format(Locale.US, "%c", 0xf5), String.format(Locale.US, "%c", 0xf6), String.format(Locale.US, "%c", 0x14d),
            String.format(Locale.US, "%c", 0x153), String.format(Locale.US, "%c", 0xb0), String.format(Locale.US, "%c", 0xf9), String.format(Locale.US, "%c", 0xfa),
            String.format(Locale.US, "%c", 0xfb), String.format(Locale.US, "%c", 0xfc), String.format(Locale.US, "%c", 0x16b), String.format(Locale.US, "%c", 0xff), "Clr(F1)", "123", "Space", "OK",
            "Français"};
    private String capsFraSpecialBuffer[] = {String.format(Locale.US, "%c", 0xc0), String.format(Locale.US, "%c", 0xc1), String.format(Locale.US, "%c", 0xc2), String.format(Locale.US, "%c", 0xc3),
            String.format(Locale.US, "%c", 0xc4), String.format(Locale.US, "%c", 0xc5), String.format(Locale.US, "%c", 0xc6), String.format(Locale.US, "%c", 0x100),
            String.format(Locale.US, "%c", 0xaa), String.format(Locale.US, "%c", 0x106), String.format(Locale.US, "%c", 0x10c), String.format(Locale.US, "%c", 0xc7),
            String.format(Locale.US, "%c", 0xc8), String.format(Locale.US, "%c", 0xc9), String.format(Locale.US, "%c", 0xca), String.format(Locale.US, "%c", 0xcb),
            String.format(Locale.US, "%c", 0x112), String.format(Locale.US, "%c", 0x116), String.format(Locale.US, "%c", 0x118), String.format(Locale.US, "%c", 0x12a),
            String.format(Locale.US, "%c", 0xcc), String.format(Locale.US, "%c", 0xcd), String.format(Locale.US, "%c", 0xce), String.format(Locale.US, "%c", 0xcf),
            String.format(Locale.US, "%c", 0x12e), String.format(Locale.US, "%c", 0xd8), String.format(Locale.US, "%c", 0xd2), String.format(Locale.US, "%c", 0xd3),
            String.format(Locale.US, "%c", 0xd4), String.format(Locale.US, "%c", 0xd5), String.format(Locale.US, "%c", 0xd6), String.format(Locale.US, "%c", 0x14c),
            String.format(Locale.US, "%c", 0x152), String.format(Locale.US, "%c", 0xb0), String.format(Locale.US, "%c", 0xd9), String.format(Locale.US, "%c", 0xda),
            String.format(Locale.US, "%c", 0xdb), String.format(Locale.US, "%c", 0xdc), String.format(Locale.US, "%c", 0x16a), String.format(Locale.US, "%c", 0xff), "Clr(F1)", "123", "Space", "OK",
            "Français"};
    private String spaSpecialBuffer[] = {"ą", "à", "á", "â", "ã", "ä", "å", "æ", "ā", "ª", "ć", "č", "ç", "è", "é", "ê", "ë", "ē", "ė", "ę", "ī", "ì", "í", "î", "ï", "į", "ø", "ò", "ó", "ô", "õ",
            "ö", "ō", "œ", "°", "ù", "ú", "û", "ü", "ū", "ń", "Clr(F1)", "123", "Space", "OK", "Español"};

    private String capsSpanSpecialBuffer[] = {"Ą", "À", "Á", "Â", "Ã", "Ä", "Å", "Æ", "Ā", "ª", "Ć", "Č", "Ç", "È", "É", "Ê", "Ë", "Ē", "Ė", "Ę", "Ī", "Ì", "Í", "Î", "Ï", "Į", "Ø", "Ò", "Ó", "Ô",
            "Õ", "Ö", "Ō", "Œ", "°", "Ù", "Ú", "Û", "Ü", "Ū", "Ń", "Clr(F1)", "123", "Space", "OK", "Español"};

    private String ItaSpecialBuffer[] = {"à", "á", "â", "ã", "ä", "å", "æ", "ā", "ª", "è", "é", "ê", "ë", "ē", "ė", "ę", "ī", "ì", "í", "î", "ï", "į", "ø", "ò", "ó", "ô", "õ", "ö", "ō", "œ", "°",
            "ù", "ú", "û", "ü", "ū", "Clr(F1)", "123", "Space", "OK", "Italiano"};
    private String capsItaSpecialBuffer[] = {"À", "Á", "Â", "Ã", "Ä", "Å", "Æ", "Ā", "ª", "È", "É", "Ê", "Ë", "Ē", "Ė", "Ę", "Ī", "Ì", "Í", "Î", "Ï", "Į", "Ø", "Ò", "Ó", "Ô", "Õ", "Ö", "Ō", "Œ",
            "°", "Ù", "Ú", "Û", "Ü", "Ū", "Clr(F1)", "123", "Space", "OK", "Italiano"};
    private String RussCharBuffer[] = {"й", "ц", "у", "к", "е", "н", "г", "ш", "щ", "з", "х", "ъ", "ф", "ы", "в", "а", "п", "р", "о", "л", "д", "ж", "э", "я", "ч", "с", "м", "и", "т", "ь", "б", "ю",
            "123", "Space", "OK", "Русский", "Clr(F1)"};
    private String capsRussCharBuffer[] = {"Й", "Ц", "У", "К", "Е", "Н", "Г", "Ш", "Щ", "З", "Х", "Ъ", "Ф", "Ы", "В", "А", "П", "Р", "О", "Л", "Д", "Ж", "Э", "Я", "Ч", "С", "М", "И", "Т", "Ь", "Б",
            "Ю", "123", "Space", "OK", "Русский", "Clr(F1)"};
    private String GreekCharBuffer[] = {";", "ς", "ε", "ρ", "τ", "υ", "θ", "ι", "ο", "π", "α", "σ", "δ", "φ", "γ", "η", "ξ", "κ", "λ", "ζ", "χ", "ψ", "ω", "β", "ν", "μ",};
    private String capsGreekCharBuffer[] = {":", "΅", "Ε", "Ρ", "Τ", "Υ", "Θ", "Ι", "Ο", "Π", "Α", "Σ", "Δ", "Φ", "Γ", "Η", "Ξ", "Κ", "Λ", "Ζ", "Χ", "Ψ", "Ω", "Β", "Ν", "Μ",};
    private String PolSpecialBuffer[] = {"ą", "à", "á", "â", "ã", "ä", "å", "æ", "ā", "ć", "č", "ç", "è", "é", "ê", "ë", "ē", "ė", "ę", "ø", "ò", "ó", "ô", "õ", "ö", "ō", "œ", "ł", "ń", "ň", "ź",
            "ż", "ž",};
    private String capsPolSpecialBuffer[] = {"Ą", "À", "Á", "Â", "Ã", "Ä", "Å", "Æ", "Ā", "Ć", "Č", "Ç", "È", "É", "Ê", "Ë", "Ē", "Ė", "Ę", "Ø", "Ò", "Ó", "Ô", "Õ", "Ö", "Ō", "Œ", "Ł", "Ń", "Ň",
            "Ź", "Ż", "Ž",};
}
