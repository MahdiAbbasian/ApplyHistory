-keep class **_Impl { *; }
-keep class **_Factory { *; }
-keep class **Dao { *; }
-keep class androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-dontwarn org.jetbrains.annotations.NotNull
-dontwarn org.jetbrains.annotations.Nullable
