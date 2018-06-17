<!DOCTYPE html>
<html>
<head>
	<title>Send Message</title>
</head>
<body>
	Content: <input type="text" name="content" id="content"><br>
	User ID: <input type="text" name="userID" id="userID"><br>
	Conversation ID: <input type="text" name="conversationID" id="conversationID"><br>

	<input type="submit" name="submit" onclick="submit()" id="submit">
	
	<div id="result"></div>

	<script type="text/javascript">
		function submit() {
			var content = $('#content').val();
			var userID = $('#userID').val();
			var convID = $('#conversationID').val();

			window.alert('cookie=a&' + 'content=' + content + '&userID=' + userID + '&conversationID=' + convID);

			$.ajax({
				type: 'POST',
				url: '/api_add_message.php',
				data: 'cookie=a&' + 'content=' + content + '&userID=' + userID + '&conversationID=' + convID,
				success : function(result) {
					$('#result').html(result);
				}
			});
		}
	</script>
</body>
</html>