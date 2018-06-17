//The request queue
RequestQueue queue = Volley.newRequestQueue([getContext()/this.getContext()]);


//The request..
StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_start_conv),
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("true")){
                    //Call fab's conv list with the id of the conv or smth

                }
                else if (response.contains("false")) {
                    //Put invalid username error prompt
                    String line = "";
                    String error_txt = "";

                    error_txt = response.substring(response.indexOf("false") + 6);

                    Toast.makeText(getActivity(), error_txt, Toast.LENGTH_LONG).show();

                }

            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //put connection problem or smth
            }
        }) {
    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("login", mUsername);
        params.put("cookie", mCookie);
        params.put("target", mTargetUsername);
        return params;
    }

};


queue.add(stringRequest);