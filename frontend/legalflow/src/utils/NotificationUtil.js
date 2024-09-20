export default class NotificationUtil {
  static returnFeedbackMessage($q, notifyMessage, type, color) {
    $q.notify({
      type: type,
      color: color,
      message: notifyMessage,
      position: "bottom-right",
    });
  }
}
