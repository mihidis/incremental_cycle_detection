# incremental_cycle_detection
Το παρών project αποτελεί διπλωματική εργασία του Χρήστου Μιχίδη.

θέμα της εργασίας είναι η υλοποιήση του βέλτιστου αλγορίθμου εύρεσης κύκλου και διατήρησης τοπολογικής διάταξης σε αυξητικό και αραιό κατευθυνόμενο γράφημα ([BK18]: Sayan Bhattacharya και Janardhan Kulkarni “An Improved 
Algorithm for Incremental Cycle Detection and Topological Ordering in 
Sparse Graphs”).

Η υλοποιήση του αλγορίθμου γίνεται σε JAVA. Για την χρήση του κώδικα ο φάκελος Project διαθέτει τα αρχεία σε μορφές java, class για να τρέξουν σε περιβάλλον IDE (πχ Eclipse). Δεν επισυνάπτονται όλα τα αρχεία που παραδόθηκαν στην διπλωματική, παρά μόνο ο κώδικας. 


ΕΠΕΞΗΓΗΣΗ ΤΟΥ ΚΩΔΙΚΑ: O αλγόριθμος θέλει να πετύχει με τυχαιοκρατικό τρόπο την βέλτιστη πολυπλοκότητα του προβλήματος (προτείνεται να έχει διαβαστεί το πρόβλημα το οποίο λύνει ο αλγόριθμος BK18 καθώς και την εργασία του για τον λόγο υλοποίησης συγκεκριμένων δομών δεδομένων). Ο παρών κώδικας δημιουργεί ένα γράφημα με 10 κόμβους (ο αριθμός των κόμβων αλλάζει στις πρώτες γραμμές της main μεθόδου) και δημιουργεί μία τοπολογική ταξινόμηση. θεωρούμε βήμα του αλγορίθμου την εισαγωγή μίας ακμής. Σε κάθε βήμα, τοποθετήτε η ακμή στο γράφημα και ενημερώνεται η τοπολογική ταξινόμηση και αν με την εισαγωγή της ακμής δημιουργείται κύκλος στο γράφημα. Όσο δεν υπάρχει κύκλος προχωράμε σε επόμενο βήμα. Αν υπάρχει κύκλος τερματίζει. Ο αλγόριθμος, ως τυχαιοκρατικός, υπάρχει πιθανότητα να μην δώσει σωστή τοπολογική ταξινόμηση ή να υπάρχει κύκλος χωρίς να τον εντοπίσει. Αλλά αυτό πρόκειται για σφάλμα μη υπολογίσιμο επειδή έχει χαμηλή πιθανότητα λάθους (λόγω αραιού γραφήματος), αλλά επίσης και επειδή πετυχαίνει βέλτιστο χρόνο Ο(n √ m). 
Το όνομα κάθε κλάσης επεξηγή τη λειτουργεία της.
 
