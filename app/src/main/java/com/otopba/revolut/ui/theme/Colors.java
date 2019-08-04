package com.otopba.revolut.ui.theme;

public class Colors {

    public final int mainBackgroundColor;
    public final int  rippleColor;
    public final int moonColor;
    public final int hintTextColor;
    public final int subtitleTextColor;
    public final int titleTextColor;
    public final int accentColor;

    private Colors(Builder builder) {
        this.mainBackgroundColor = builder.mainBackgroundColor;
        this.rippleColor = builder.rippleColor;
        this.moonColor = builder.moonColor;
        this.titleTextColor = builder.titleTextColor;
        this.subtitleTextColor = builder.subtitleTextColor;
        this.hintTextColor = builder.hintTextColor;
        this.accentColor = builder.accentColor;
    }

    public static class Builder {

        private int mainBackgroundColor;
        private int rippleColor;
        private int moonColor;
        private int titleTextColor;
        private int subtitleTextColor;
        private int hintTextColor;
        private int accentColor;

        public Builder setMainBackgroundColor(int mainBackgroundColor) {
            this.mainBackgroundColor = mainBackgroundColor;
            return this;
        }

        public Builder setRippleColor(int rippleColor) {
            this.rippleColor = rippleColor;
            return this;
        }

        public Builder setMoonColor(int moonColor) {
            this.moonColor = moonColor;
            return this;
        }

        public Builder setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder setSubtitleTextColor(int subtitleTextColor) {
            this.subtitleTextColor = subtitleTextColor;
            return this;
        }

        public Builder setHintTextColor(int hintTextColor) {
            this.hintTextColor = hintTextColor;
            return this;
        }

        public Builder setAccentColor(int accentColor) {
            this.accentColor = accentColor;
            return this;
        }

        public Colors build() {
            return new Colors(this);
        }
    }

}