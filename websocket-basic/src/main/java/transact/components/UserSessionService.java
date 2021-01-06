package transact.components;

import static transact.constants.CommonConstants.pmlIdsToSessionMap;
import static transact.constants.CommonConstants.userSessionSessionAndPmlIdsMap;

import javax.websocket.Session;
import transact.beans.SessionAndPmlIds;
import transact.beans.UserSession;
import transact.beans.enums.Type;
import transact.beans.request.SubscriptionReq;

public class UserSessionService {

  public void handleUserMessage(Session session, SubscriptionReq message){
    UserSession userSession = new UserSession( session );
    userSessionSessionAndPmlIdsMap.putIfAbsent
        ( userSession, new SessionAndPmlIds( userSession ) );
    if (message.getType() == Type.S){
      handleSubscribeEvent( userSession, message.getPmlId() );
    }
    else{
      handleUnsubscribeEvent( userSession, message.getPmlId() );
    }
  }
  private void handleSubscribeEvent(UserSession session, Long pmlId){
    userSessionSessionAndPmlIdsMap.get(session).getPmlIds().add( pmlId );
    pmlIdsToSessionMap.get( pmlId ).add( session );
  }

  private void handleUnsubscribeEvent(UserSession session, Long pmlId){
    userSessionSessionAndPmlIdsMap.get(session).getPmlIds().remove( pmlId );
    pmlIdsToSessionMap.get( pmlId ).remove( session );
  }

  public void handleDisconnectEvent(Session session){
    UserSession userSession = new UserSession( session );
    userSessionSessionAndPmlIdsMap.get(userSession).getPmlIds().forEach(
        pmlId -> {
          pmlIdsToSessionMap.get(pmlId).remove( userSession );
    });
    userSessionSessionAndPmlIdsMap.remove( userSession );
  }


}
