
package com.example.calendarquickstart;

import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class CreateEventTask extends AsyncTask<Void, Void, Void> {
    Calendar mService;

    CreateEventTask(Calendar mService) {
        this.mService = mService;
    }

    @Override
    protected Void doInBackground(Void... params) {
        addCalendarEvent();

        return null;
    }

    public void addCalendarEvent() {
        Event event = new Event()
                .setSummary("Google I/O 2020")
                .setLocation("800 Howard St., San Francisco, CA 94103")
                .setDescription("A chance to hear more about Google's developer products.");

        DateTime startDateTime = new DateTime("2020-12-18T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(TimeZone.getDefault().getID());
        event.setStart(start);

        DateTime endDateTime = new DateTime("2020-12-19T09:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(TimeZone.getDefault().getID());
        event.setEnd(end);

        String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail("cygnet.km.patel@gmail.com"),
                //new EventAttendee().setEmail("sbrin@example.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        try {
            mService.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
