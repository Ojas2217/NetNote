
| **Key**          | **Value**        |  
| -----------------|------------------|  
| **Date**         | 22-1-2025        |  
| **Time**         | Wed, 14:45-15:30 |  
| **Location**     | DW IZ1           |  
| **Chair**        | Vilius   |  
| **Minute Taker** | Kaan      |  
| **Attendees**    | Vilius Birgeris  |  
|                  | Sander de Jong   |  
|                  | Otte Hamoen      |  
|                  | Kaan Murzoglu    |  
|                  | Ojas Pandey      |  
|                  | Quinten Rensen   |  
  
  
## Agenda Items  
1. **Opening by Chair** (1 min)  
2. **Check-In**: How is everyone doing? (2 min)  
3. **Announcements by the Team** (2 min)  
4. **Approval of the Agenda**: Does anyone have any additions? (1 min)  
5. **Approval of Last Minutes**: Did everyone read the minutes from the previous meeting? (1 min)  
6. **Announcements by the TA** (3 min)  
7. **Presentation of the Current App to TA** (2 min)  
8. **Talking Points**:  
   - Discuss what everyone worked on (3 min)  
   - Discuss upcoming exams (1 min)
	   - Will people have time to code?
   - Are there any bugs impacting basic requirements are not in the **Appendix (below)**? (2 min)  
   - Server-side lines of code update. Sander and Kaan (1 min) 
   - Discuss **Very Important Issues** in **Appendix** (25 min)  
	   - Any more important issues?
	   - Discuss priorities by grade points.
	   - Distribute to people.
	- Discuss **Deadline** (by Friday would be best). (5 min)
		- Is it possible/realistic?
9. **Summarize Action Points**: Who, what, when? (2 min)  
10. **Feedback Round**: What went well and what can be improved next time? (2 min)  
11. **Planned Meeting Duration != Actual Duration**: Where/why did we mis-estimate? (1 min)  
12. **Question Round**: Does anyone have anything to add before the meeting closes? (3 min)  
13. **Closure** (1 min)

## Appendix
### Very Important Issues
#### Basic [1 pts]
1. No way to "see all existing notes on the server". (Ask TA if all notes from selected collections count)
2. Optimization: Not every key stroke should be sent.
3. Need to be no bugs.
4. Duplicate note titles possible with transferring from collections. **1 pts** (maybe, if collections impact basic requirements in this way)

#### Collections [**0.1 pts**] + **0.1 pts** if bonus
1. Default collections need to be changeable.
2. If a single one is selected, it should be default.
3. Need to be able to select only some collections to view.
4. Collection import window: add existing collections from the server.
5. Collection import window: status messages. **(Server-side lines)**
6. Collection import window: 2 names for a Collection: local name and server-side name.
7. Edit Collection title?
8. "[Default]" Needs to be added for accessibility.

### Web Sockets  [**0.1 pts**] + **0.1 pts** if bonus
1. Handle a local collection being deleted on the server. Make sure default or non-default collection being deleted doesn't break anything.
2. Web Sockets should ensure that **all** changes to relevant Notes and Collections result in automatic refresh. 

### Internationalization **[0.2 pts]**
1.  Collections are not internationalized. **0.1 pts**
2.  Meaningful addition **0.1 pts**
---
### Accessibility [0.2 pts]
1. File-Edit-Help should be removed. **0 pts**
2. Add Remove button order should be the same in Collections and MainScreen. **0.1 pts***
3. Editing title should update the UI (currently title remains the old one on top) **0.1 pts***
4. Delete dialog is not in dark mode. **0.1 pts****
5. Dark mode collection window arrows are almost invisible. **0.1 pts****
6. Collection add menu should have Add on the right side. **0.1 pts***
7. Dark mode still breaks when switching languages and needs to be switched on an off to work. **0.1 pts****
8. No feedback on refresh. **0 pts**
'*' means pts worth when combined.

---
### Other 
Technology **[0.3 pts]**
1. NoteSearchResult and SceneInfo should have @Inject **0.1 pts**
2. UI code separate from communicating with server **0.1 pts**
	3. (2 was prerequisite) Business logic separated from UI and communication code **0.1 pts**

Tasks and Planning **[1.165 pts]**
3. Time estimating and small issues **0.125 pts**
	2.  Labels (might be too late for epics, but if they count, we have this one) **0.125 pts**
4. Labels **0.125 pts** 
5. Time tracking with all issues **0.25 pts**
6. Time estimate changing (might not be possible, unless we already have this) **0.125 pts**
7. MR descriptions and small-ish MRs **0.165 pts**
8. Code reviews (probably lost) **0.25 pts** 

