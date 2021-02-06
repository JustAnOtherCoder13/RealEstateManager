package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.picone.core.data.Generator;
import com.picone.core.data.RealEstateManagerRoomDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.picone.core.utils.ConstantParameters.URI_ITEM;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PropertyContentProviderTest {

    private ContentResolver contentResolver;

    @Before
    public void setup() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateManagerRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        contentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void getPropertyInformationShouldReturnPropertyValue() {
        final Cursor cursor =
                contentResolver.query(ContentUris.withAppendedId(URI_ITEM, Generator.generatePropertiesInformation().get(0).getId()), null, "property", null, null);
        assertNotNull(cursor);
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getInt(cursor.getColumnIndexOrThrow("id")), is(Generator.generatePropertiesInformation().get(0).getId()));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("propertyType")), is(Generator.generatePropertiesInformation().get(0).getPropertyType()));
    }

    @Test
    public void getAllPropertiesInformationShouldReturnAllValues() {
        final Cursor cursor =
                contentResolver.query(ContentUris.withAppendedId(URI_ITEM, 0), null, "properties", null, null);
        assertNotNull(cursor);
        assertThat(cursor.getCount(), is(2));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getInt(cursor.getColumnIndexOrThrow("id")), is(Generator.generatePropertiesInformation().get(0).getId()));
        assertThat(cursor.moveToNext(), is(true));
        assertThat(cursor.getInt(cursor.getColumnIndexOrThrow("id")), is(Generator.generatePropertiesInformation().get(1).getId()));
    }

    @Test
    public void getPropertyLocationForPropertyIdShouldReturnValue() {
        final Cursor cursor =
                contentResolver.query(ContentUris.withAppendedId(URI_ITEM, Generator.generatePropertiesInformation().get(0).getId()), null, "location", null, null);
        assertNotNull(cursor);
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("address")), is(Generator.generatePropertyLocation().get(0).getAddress()));
    }
}
