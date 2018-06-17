
// when you call your new intent
Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
Bundle b = new Bundle();
b.putInt("key", 1); //Your id
intent.putExtras(b); //Put your id to your next Intent
startActivity(intent);
finish();

// your new activity 
Bundle b = getIntent().getExtras();
int value = -1; // or other values
if(b != null)
value = b.getInt("key");

