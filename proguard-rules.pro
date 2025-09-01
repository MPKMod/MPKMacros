-verbose
-dontwarn java.**
-dontwarn org.apache.logging.log4j.**
-dontwarn io.github.kurrycat.mpkmod.macros.MPKMacros

-keep class io.github.kurrycat.mpkmod.module.macros.MPKMacros {
    public void init();
    public void loaded();
}

-repackageclasses 'io.github.kurrycat.mpkmod.module.macros'

-printmapping build/libs/map.out

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
