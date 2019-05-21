class VideoLoaderTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                VrVideoView.Options options = new VrVideoView.Options();
                options.inputType = VrVideoView.Options.TYPE_MONO;
                mVrVideoView.loadVideoFromAsset("check.MP4", options);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "This is my Toast message!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }

