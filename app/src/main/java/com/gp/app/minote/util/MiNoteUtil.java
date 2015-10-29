package com.gp.app.minote.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.EditText;
import android.widget.TextView;

import com.gp.app.minote.data.Event;
import com.gp.app.minote.data.Note;
import com.gp.app.minote.data.NoteItem;
import com.gp.app.minote.data.TextNote;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiNoteUtil {
//    private static final String DELEMITER = "&&(<DELIMITER>)&&";

    private static final String NOTE_PATTERN = "<Note>(.*?)</Note>";

    private static final String NOTE_ID_PATTERN = "<NoteId>(.*?)</NoteId>";

    private static final String NOTE_COLOUR_PATTERN = "<NoteColour>(.*?)</NoteColour>";

    private static final String NOTE_CREATION_TIME_PATTERN = "<NoteCreationTime>(.*?)</NoteCreationTime>";

    private static final String NOTE_LAST_EDIT_ITEM_PATTERN = "<NoteLastEditTime>(.*?)</NoteLastEditTime>";

    private static final String NOTE_ITEM_PATTERN = "<NoteItem>(.*?)</NoteItem>";

    private static final String NOTE_ITEM_TEXT_COLOUR_PATTERN = "<NoteItemTextColour>(.*?)</NoteItemTextColour>";

    private static final String NOTE_ITEM_TEXT_PATTERN = "<NoteItemText>(.*?)</NoteItemText>";

    private static final String NOTE_ITEM_IMAGE_NAME_PATTERN = "<NoteItemImageName>(.*?)</NoteItemImageName>";

    private static final String NOTE_ITEM_IS_TITLE_PATTERN = "<IsTitle>(.*?)</IsTitle>";

    private static final String EVENT_PATTERN = "<Event>(.*?)</Event>";

    private static final String EVENT_NAME_PATTERN = "<EventName>(.*?)</EventName>";

    private static final String EVENT_LOCATION_PATTERN = "<EventLocation>(.*?)</EventLocation>";

    private static final String EVENT_START_DATE_PATTERN = "<EventStartDate>(.*?)</EventStartDate>";

    private static final String EVENT_START_TIME_PATTERN = "<EventStartTime>(.*?)</EventStartTime>";

    private static final String NOTE_END_DATE_PATTERN = "<EventEndDate>(.*?)</EventEndDate>";

    private static final String NOTE_END_TIME_PATTERN = "<EventEndTime>(.*?)</EventEndTime>";

    private static final String VALUE_EXTRACTER_PATTERN = ">(.*?)<";

    public static String writeEvent(Event event) {
        StringBuilder sb = new StringBuilder();

        sb.append("<Event>");

        sb.append("<EventName>" + event.getEventName() + "</EventName>");

        sb.append("<EventLocation>" + event.getLocation() + "</EventLocation>");

        sb.append("<EventStartDate>" + event.getStartDate() + "</EventStartDate>");

        sb.append("<EventStartTime>" + event.getStartTime() + "</EventStartTime>");

        sb.append("<EventEndDate>" + event.getEndDate() + "</EventEndDate>");

        sb.append("<EventEndTime>" + event.getEndTime() + "</EventEndTime>");

        sb.append("</Event>");

        return sb.toString();
    }

    public static List<Note> createNotes(String noteText) {
        List<Note> notes = new ArrayList<Note>();

        Pattern notePattern = Pattern.compile(NOTE_PATTERN);

        Matcher noteMatcher = notePattern.matcher(noteText);

        while (noteMatcher.find()) {
            notes.add(createNote(noteMatcher.group(1)));
        }

        Pattern eventPattern = Pattern.compile(EVENT_PATTERN);

        Matcher eventMatcher = eventPattern.matcher(noteText);

        while (eventMatcher.find()) {
            notes.add(createEvent(eventMatcher.group(1)));
        }

        return notes;
    }

    public static List<Event> createEvents(String eventText) {
        Pattern r = Pattern.compile(EVENT_PATTERN);

        List<Event> notes = new ArrayList<>();

        Matcher matcher = r.matcher(eventText);

        while (matcher.find()) {
            notes.add(createEvent(matcher.group(1)));
        }

        return notes;
    }

    private static Event createEvent(String group) {
        String noteString = group;

        String eventName = extractNoteAttributeValues(noteString, EVENT_NAME_PATTERN);

        String eventLocation = extractNoteAttributeValues(noteString, EVENT_LOCATION_PATTERN);

        String eventStartDate = extractNoteAttributeValues(noteString, EVENT_START_DATE_PATTERN);

        String eventStartTime = extractNoteAttributeValues(noteString, EVENT_START_TIME_PATTERN);

        String eventEndDate = extractNoteAttributeValues(noteString, NOTE_END_DATE_PATTERN);

        String eventEndTime = extractNoteAttributeValues(noteString, NOTE_END_TIME_PATTERN);

        Event event = new Event(eventName, eventLocation, eventStartDate, eventStartTime, eventEndDate, eventEndTime);

        return event;
    }

    private static TextNote createNote(String group) {
        TextNote note = new TextNote();

        String noteString = group;

        String noteId = extractNoteAttributeValues(noteString, NOTE_ID_PATTERN);

        note.setNoteId(Integer.valueOf(noteId));

        String noteColourValue = extractNoteAttributeValues(noteString, NOTE_COLOUR_PATTERN);

        note.setNoteColor(Integer.valueOf(noteColourValue));

        String noteCreationTime = extractNoteAttributeValues(noteString, NOTE_CREATION_TIME_PATTERN);

        note.setCreationTime(Long.valueOf(noteCreationTime));

        String noteLastEditTime = extractNoteAttributeValues(noteString, NOTE_LAST_EDIT_ITEM_PATTERN);

        note.setLastEditedTime(Long.valueOf(noteLastEditTime));

        List<NoteItem> noteItems = extractNoteItems(noteString);

        note.setNoteItems(noteItems);

        return note;
    }

    private static List<NoteItem> extractNoteItems(String noteString) {
        Pattern r = Pattern.compile(NOTE_ITEM_PATTERN);

        Matcher matcher = r.matcher(noteString);

        String noteAttributeValue = null;

        List<NoteItem> noteItems = new ArrayList<>();

        while (matcher.find()) {
            String noteAttributePattern = matcher.group(1);

            String noteITemTextColourValue = extractNoteAttributeValues(noteAttributePattern, NOTE_ITEM_TEXT_COLOUR_PATTERN);

            String noteItemTextValue = extractNoteAttributeValues(noteAttributePattern, NOTE_ITEM_TEXT_PATTERN);

            String noteItemImageName = extractNoteAttributeValues(noteAttributePattern, NOTE_ITEM_IMAGE_NAME_PATTERN);

            boolean isTitle = Boolean.valueOf(extractNoteAttributeValues(noteAttributePattern, NOTE_ITEM_IS_TITLE_PATTERN));

            NoteItem item = new NoteItem(noteItemTextValue, noteItemImageName);

            item.setIsTitle(isTitle);

            item.setTextColour(Integer.valueOf(noteITemTextColourValue));

            noteItems.add(item);
        }

        return noteItems;
    }

    private static String extractNoteAttributeValues(String group, String pattern) {
        Pattern r = Pattern.compile(pattern);

        Matcher matcher = r.matcher(group);

        String noteAttributeValue = null;

        if (matcher.find()) {
            noteAttributeValue = matcher.group(1);

            // noteAttributeValue = extractValueFromExpression(noteAttributePattern);
        }

        return noteAttributeValue;
    }

    private static String extractValueFromExpression(String noteIdPattern) {
        Pattern pattern = Pattern.compile(VALUE_EXTRACTER_PATTERN);

        Matcher noteIdMatcher = pattern.matcher(noteIdPattern);

        String value = null;

        if (noteIdMatcher.find()) {
            value = noteIdMatcher.group(1);
        }

        return value;
    }


    private static String writeTextNoteItem(List<NoteItem> noteItems) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < noteItems.size(); i++) {
            NoteItem item = noteItems.get(i);

            sb.append("<NoteItem>");

            sb.append("<NoteItemTextColour>" + item.getTextColour() + "</NoteItemTextColour>");

            sb.append("<IsTitle>" + item.isTitle() + "</IsTitle>");

            if (item.getText() != null && !item.getText().equals("")) {
                sb.append("<NoteItemText>" + item.getText() + "</NoteItemText>");
            }

            if (item.getImageName() != null && !item.getImageName().equals("")) {
                sb.append("<NoteItemImageName>" + item.getImageName() + "</NoteItemImageName>");
            }

            sb.append("</NoteItem>");
        }

        return sb.toString();
    }

    public static String createInternalXMLFilePath() {
        return Environment.getExternalStorageDirectory() + "/" + MiNoteConstants.PROFESSIONAL_PA_XML_FILE_NAME;
    }

    public static String createExportedFilePath() {
        return Environment.getExternalStorageDirectory() + MiNoteConstants.PROFESSIONAL_PA_EXPORT_PATH
                + "//" + MiNoteConstants.PROFESSIONAL_PA_XML_FILE_NAME;
    }

    public static String createExportedDirectoryPath() {
        return Environment.getExternalStorageDirectory() + MiNoteConstants.PROFESSIONAL_PA_EXPORT_PATH;
    }

    public static long parseDateAndTimeString(String time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Date creationDate = null;

        try {
            creationDate = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return creationDate == null ? 0 : creationDate.getTime();
    }

    public static String createStringForDate(long creationTimeAndDate, String format) {
        Date creationTime = new Date(creationTimeAndDate);

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        String creationDate = formatter.format(creationTime);
        return creationDate;
    }

    public static String createImageNameFromTime() {
        Date creationTime = new Date(System.currentTimeMillis());

        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyMMddHHmmss");

        String creationDate = formatter.format(creationTime);
        return creationDate;
    }

    public static long parseDateAndTimeImageNameString(String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");

        Date creationDate = formatter.parse(time);

        return creationDate.getTime();
    }

    public static long createTime(String date, String time) {
        long parsedTime = 0l;

        if (date == null || time == null) {
            return parsedTime;
        }

        String[] timeTokens = null;

        String[] dateToken = null;


        timeTokens = time.split(":");

        dateToken = date.split("/");

        String createdStartTime = dateToken[2] + dateToken[1] + dateToken[0] + timeTokens[0] + timeTokens[1];

        parsedTime = MiNoteUtil.parseDateAndTimeString(createdStartTime, "yyyyMMddHHmm");


        return parsedTime;
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static long getDayStartTime(long lastModifiedTime) {
        String date = createStringForDate(lastModifiedTime, "dd/MM/yyyy");

        long time = createTime(date, "00:00");

        System.out.println("getDayStartTime -> time=" + time + " date=" + date);

        return time;
    }

    public static void setCursorColor(EditText editText, int color) {
        try {

            //TODO to be checked and improved.
//	    	TODO Below commented 3 lines will also work but the colour of cursor will not be changed.
//	    	Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
//	        f.setAccessible(true);
//	        f.set(yourEditText, R.drawable.cursor);

            final Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            final int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            final Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            final Object editor = fEditor.get(editText);
            final Class<?> clazz = editor.getClass();
            final Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            final Drawable[] drawables = new Drawable[2];
            drawables[0] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
            drawables[1] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (final Throwable ignored) {
            //TODO improve
            ignored.printStackTrace();
        }
    }

    public static String writeTextNote(TextNote note) {
        StringBuilder sb = new StringBuilder();

        sb.append("<Note>");

        sb.append("<NoteId>" + note.getId() + "</NoteId>");

        sb.append("<NoteColour>" + note.getNoteColor() + "</NoteColour>");

        sb.append("<NoteCreationTime>" + note.getCreationTime() + "</NoteCreationTime>");

        sb.append("<NoteLastEditTime>" + note.getLastEditedTime() + "</NoteLastEditTime>");

        sb.append(writeTextNoteItem(note.getNoteItems()));

        sb.append("</Note>");

        return sb.toString();
    }
}
